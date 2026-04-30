package io.github.iweidujiang.dsv4.sqloptimizer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据库元数据服务
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
public class DatabaseMetadataService {
    private final JdbcTemplate jdbcTemplate;

    /**
     * 获取数据库中所有表名、列信息、现有索引，用于增强提示词
     */
    public String getSchemaContext() {
        StringBuilder sb = new StringBuilder();
        try {
            // 获取所有表
            List<String> tables = jdbcTemplate.queryForList(
                    "SHOW TABLES", String.class);
            for (String table : tables) {
                sb.append("表 ").append(table).append(":\n");
                // 列信息
                List<Map<String, Object>> columns = jdbcTemplate.queryForList(
                        "SHOW COLUMNS FROM " + table);
                for (Map<String, Object> col : columns) {
                    sb.append("  列: ").append(col.get("Field")).append(" 类型: ").append(col.get("Type"))
                            .append(" 是否可空: ").append(col.get("Null")).append("\n");
                }
                // 现有索引
                List<Map<String, Object>> indexes = jdbcTemplate.queryForList(
                        "SHOW INDEX FROM " + table);
                if (!indexes.isEmpty()) {
                    sb.append("  已有索引: ");
                    String idxStr = indexes.stream()
                            .map(idx -> idx.get("Key_name").toString())
                            .distinct()
                            .collect(Collectors.joining(", "));
                    sb.append(idxStr).append("\n");
                }
            }
        } catch (Exception e) {
            log.warn("获取元数据失败", e);
            return "无法获取数据库结构";
        }
        return sb.toString();
    }
}
