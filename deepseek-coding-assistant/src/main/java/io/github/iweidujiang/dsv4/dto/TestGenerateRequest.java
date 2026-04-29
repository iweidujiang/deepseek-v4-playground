package io.github.iweidujiang.dsv4.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * 生成测试类请求
 *
 * 项目名称：deepseek-coding-assistant
 * 创建时间：2026-04-27 23:43
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 */
@Data
public class TestGenerateRequest {
    @NotBlank(message = "代码内容不能为空")
    private String code;
    private String methodName;
    private String returnType = "void";
    private List<String> parameterTypes;
}
