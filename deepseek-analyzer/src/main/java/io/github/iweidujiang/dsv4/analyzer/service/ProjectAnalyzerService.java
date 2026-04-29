package io.github.iweidujiang.dsv4.analyzer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.iweidujiang.dsv4.analyzer.config.DeepSeekProperties;
import io.github.iweidujiang.dsv4.analyzer.dto.AnalysisReport;
import io.github.iweidujiang.dsv4.analyzer.entity.SourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目分析业务类
 * <p>
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 * <p>
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * <a href="https://github.com/iweidujiang">GitHub</a>
 * <p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectAnalyzerService {

    private final ChatClient basicChatClient;
    private final SourceCodeCollector collector;
    private final ObjectMapper objectMapper;
    private final DeepSeekProperties deepSeekProperties;

    private static final String SYSTEM_PROMPT = """
            你是资深 Java 架构师。分析源码后按以下 JSON 输出，不要有其他内容：
            {
              "architecture": { "summary": "", "coreModules": [], "patterns": [] },
              "securityRisks": [ {"file": "", "line": 0, "risk": ""} ],
              "potentialBugs": [ {"file": "", "line": 0, "bug": ""} ],
              "suggestions": []
            }
            """;

    public AnalysisReport analyze(String projectPath, String mode) throws Exception {
        List<SourceFile> files = collector.scanJavaFiles(projectPath);
        if (files.isEmpty()) {
            throw new IllegalArgumentException("未找到任何 Java 源文件");
        }

        String userContent = collector.buildPrompt(files, mode);
        log.info("发送内容长度: {} 字符", userContent.length());

        // 先尝试主模型，失败后自动降级到备用模型
        String primaryModel = deepSeekProperties.getModels().getPrimary();
        String fallbackModel = deepSeekProperties.getModels().getFallback();
        int maxAttempts = deepSeekProperties.getRetry().getMaxAttempts();
        long backoff = deepSeekProperties.getRetry().getBackoffMillis();

        Exception lastException = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            String modelToUse = (attempt == 1) ? primaryModel : fallbackModel;
            try {
                log.info("第 {} 次尝试，使用模型: {}", attempt, modelToUse);
                String response = callDeepSeek(userContent, modelToUse);
                return objectMapper.readValue(extractJson(response), AnalysisReport.class);
            } catch (Exception e) {
                log.warn("模型 {} 调用失败: {}", modelToUse, e.getMessage());
                lastException = e;
                if (attempt < maxAttempts && backoff > 0) {
                    Thread.sleep(backoff);
                }
            }
        }
        throw new RuntimeException("所有模型均调用失败", lastException);
    }

    private String callDeepSeek(String userContent, String model) {
        return basicChatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(userContent)
                .options(OpenAiChatOptions.builder()
                        .model(model)
                        .temperature(0.1)
                        .maxTokens(8192)
                        .build())
                .call()
                .content();
    }

    private String extractJson(String resp) {
        int start = resp.indexOf('{');
        int end = resp.lastIndexOf('}');
        if (start != -1 && end > start) {
            return resp.substring(start, end + 1);
        }
        throw new RuntimeException("AI 响应未包含有效 JSON");
    }
}