package io.github.iweidujiang.dsv4.analyzer.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * 项目名称：deepseek-v4-playground
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 *
 * 分析报表
 */
@Data
public class AnalysisReport {
    private Map<String, Object> architecture;
    private List<Map<String, Object>> securityRisks;
    private List<Map<String, Object>> potentialBugs;
    private List<String> suggestions;
}
