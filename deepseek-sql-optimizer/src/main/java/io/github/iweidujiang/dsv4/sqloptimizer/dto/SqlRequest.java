package io.github.iweidujiang.dsv4.sqloptimizer.dto;

import lombok.Data;

/**
 * 发送sql请求实体类参数
 * <p>
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 * <p>
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * <a href="https://github.com/iweidujiang">GitHub</a>
 * <p>
 */
@Data
public class SqlRequest {
    private String naturalLanguage;
    private String dialect = "mysql";  // mysql, postgresql, oracle
    private String model = "deepseek-v4-pro";  // 可选的v4模型：deepseek-v4-pro 或 deepseek-v4-flash
    private boolean enableOptimize = true;
}
