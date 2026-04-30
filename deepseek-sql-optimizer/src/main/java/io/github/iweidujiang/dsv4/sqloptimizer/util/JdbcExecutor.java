package io.github.iweidujiang.dsv4.sqloptimizer.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * JDBC 执行器，用于执行 SQL 和 EXPLAIN
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
@Component
public class JdbcExecutor {
    private final JdbcTemplate jdbcTemplate;

    /**
     * 执行 EXPLAIN 并返回格式化的结果
     */
    public String explainSql(String sql, String dialect) {
        try {
            String explainSql = "EXPLAIN " + sql;
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(explainSql);
            StringBuilder sb = new StringBuilder();
            for (Map<String, Object> row : rows) {
                sb.append(row.toString()).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("EXPLAIN 执行失败", e);
            return "EXPLAIN 失败: " + e.getMessage();
        }
    }

    /**
     * 执行 SQL（仅用于测试，不返回数据）
     */
    public boolean testExecute(String sql) {
        try {
            jdbcTemplate.execute(sql);
            return true;
        } catch (Exception e) {
            log.warn("SQL 执行失败: {}", e.getMessage());
            return false;
        }
    }
}
