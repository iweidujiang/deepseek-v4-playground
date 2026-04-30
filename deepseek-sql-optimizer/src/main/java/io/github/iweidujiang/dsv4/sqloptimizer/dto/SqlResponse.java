package io.github.iweidujiang.dsv4.sqloptimizer.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 发送sql请求的响应结果类
 * <p>
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 * <p>
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * <a href="https://github.com/iweidujiang">GitHub</a>
 * <p>
 */
@Data
@Builder
public class SqlResponse {
    private String generatedSql;
    private String explainResult;
    private String optimizationAdvice;
    private String indexDdl;
    private String error;
}
