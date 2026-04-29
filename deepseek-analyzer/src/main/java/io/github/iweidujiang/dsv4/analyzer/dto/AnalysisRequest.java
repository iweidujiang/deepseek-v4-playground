package io.github.iweidujiang.dsv4.analyzer.dto;

import lombok.Data;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * 项目名称：deepseek-v4-playground
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 *
 * 分析请求
 */
@Data
public class AnalysisRequest {
    private String projectPath;
    private String mode; // "summary" 或 "full"
}
