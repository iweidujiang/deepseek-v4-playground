package io.github.iweidujiang.dsv4.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * 代码生成请求
 *
 * 项目名称：deepseek-coding-assistant
 * 创建时间：2026-04-27 22:40
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 */
@Data
public class CodeGenerateRequest {
    @NotBlank(message = "需求描述不能为空")
    private String requirement;      // 需求描述

    private String language = "java"; // 编程语言

    private String framework = "none"; // 技术框架

    private Boolean needExplanation = true; // 是否需要解释

    private Integer maxTokens = 4096;      // 最大输出长度
}
