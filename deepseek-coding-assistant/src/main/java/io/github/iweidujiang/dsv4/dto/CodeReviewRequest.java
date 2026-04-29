package io.github.iweidujiang.dsv4.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * 代码审查请求
 *
 * 项目名称：deepseek-coding-assistant
 * 创建时间：2026-04-27 22:41
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 */
@Data
public class CodeReviewRequest {
    @NotBlank(message = "代码内容不能为空")
    private String code;

    private String language = "java";

    private Boolean strictMode = false;
}
