package io.github.iweidujiang.dsv4.sqloptimizer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.iweidujiang.dsv4.sqloptimizer.dto.SqlResponse;
import io.github.iweidujiang.dsv4.sqloptimizer.util.JdbcExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * SQL 生成与优化核心服务
 * <p>
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 * <p>
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * <a href="https://github.com/iweidujiang">GitHub</a>
 * <p>
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SqlGenerateService {
    private final ChatClient chatClient;
    private final DatabaseMetadataService metadataService;
    private final JdbcExecutor jdbcExecutor;
    private final ObjectMapper objectMapper;

    @Value("${deepseek.sql.dialect:mysql}")
    private String defaultDialect;

    private static final String GENERATE_SQL_SYSTEM = """
            你是一个资深 SQL 专家。根据用户的中文需求，生成符合指定方言的 SQL 查询语句。
            输出格式必须是 JSON：{"sql": "生成的SQL语句"}
            只输出 JSON，不要包含其他说明。
            """;

    private static final String OPTIMIZE_SYSTEM = """
            你是一个数据库性能优化专家。下面是用户原始需求、生成的 SQL、以及该 SQL 在数据库中的 EXPLAIN 结果。
            请分析性能问题（如全表扫描、索引失效等），输出优化后的 SQL 和创建索引的 DDL。
            输出格式 JSON：{"optimizedSql": "...", "indexDdl": "CREATE INDEX ...", "advice": "优化建议"}
            """;

    public SqlResponse process(String naturalLanguage, String dialect, String model, boolean optimize) {
        SqlResponse.SqlResponseBuilder builder = SqlResponse.builder();

        try {
            // 1. 获取数据库元数据，增强上下文
            String schemaMeta = metadataService.getSchemaContext();

            // 2. 生成 SQL
            String generatePrompt = String.format("""
                    数据库结构如下：
                    %s
                    
                    用户需求：%s
                    数据库方言：%s
                    请生成 SQL。
                    """, schemaMeta, naturalLanguage, dialect);

            String generateResp = chatClient.prompt()
                    .system(GENERATE_SQL_SYSTEM)
                    .user(generatePrompt)
                    .options(OpenAiChatOptions.builder()
                            .model(model)
                            .temperature(0.1)
                            .build())
                    .call()
                    .content();

            Map<String, String> genMap = objectMapper.readValue(extractJson(generateResp), Map.class);
            String generatedSql = genMap.get("sql");
            builder.generatedSql(generatedSql);

            if (!optimize) {
                return builder.build();
            }

            // 3. 执行 EXPLAIN
            String explainResult = jdbcExecutor.explainSql(generatedSql, dialect);
            builder.explainResult(explainResult);

            // 4. 调用优化模型
            String optimizePrompt = String.format("""
                    原始需求：%s
                    生成的 SQL：%s
                    EXPLAIN 结果：%s
                    请给出优化建议。
                    """, naturalLanguage, generatedSql, explainResult);

            String optimizeResp = chatClient.prompt()
                    .system(OPTIMIZE_SYSTEM)
                    .user(optimizePrompt)
                    .options(OpenAiChatOptions.builder()
                            .model("deepseek-v4-flash") // 优化用 flash 降低成本
                            .temperature(0.2)
                            .build())
                    .call()
                    .content();

            Map<String, String> optMap = objectMapper.readValue(extractJson(optimizeResp), Map.class);
            builder.optimizationAdvice(optMap.getOrDefault("advice", ""));
            builder.indexDdl(optMap.getOrDefault("indexDdl", ""));

        } catch (Exception e) {
            log.error("处理失败", e);
            builder.error(e.getMessage());
        }
        return builder.build();
    }

    private String extractJson(String resp) {
        int start = resp.indexOf('{');
        int end = resp.lastIndexOf('}');
        if (start != -1 && end > start) {
            return resp.substring(start, end + 1);
        }
        throw new RuntimeException("AI 响应未包含 JSON");
    }
}
