package io.github.iweidujiang.dsv4.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * 代码审查响应类
 *
 * 项目名称：deepseek-coding-assistant
 * 创建时间：2026-04-27 23:39
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 */
@Data
@Builder
public class CodeReviewResponse {
    private int overallScore;
    private List<ReviewIssue> criticalIssues;
    private List<ReviewIssue> warnings;
    private List<String> suggestions;
    private String fixedCode;
}
