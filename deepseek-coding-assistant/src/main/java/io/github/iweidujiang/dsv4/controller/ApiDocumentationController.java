package io.github.iweidujiang.dsv4.controller;

import io.github.iweidujiang.dsv4.commom.ApiResponse;
import io.github.iweidujiang.dsv4.service.DeepSeekService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * API文档自动生成
 *
 * 项目名称：deepseek-coding-assistant
 * 创建时间：2026-04-27 23:11
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/docs")
public class ApiDocumentationController {
    private final DeepSeekService deepSeekService;

    private static final String DOC_GEN_SYSTEM_PROMPT = """
            你是一位技术文档专家，请根据提供的REST API代码生成OpenAPI 3.0规范的文档。
            
            文档必须包含:
            - API基本信息 (title, version, description)
            - 完整的路径定义
            - 请求/响应Schema
            - 认证方式说明
            - 使用示例
            
            输出格式: JSON格式的OpenAPI 3.0文档
            """;

    @PostMapping("/generate")
    public ApiResponse<String> generateApiDoc(@RequestBody String controllerCode) {
        log.info("API文档生成请求 - 代码长度: {}", controllerCode.length());
        String prompt = String.format("""
                请为以下Spring Boot Controller生成OpenAPI 3.0文档:
                
                ```java
                %s
                """, controllerCode);
        String documentation = deepSeekService.generateWithSystem(prompt, DOC_GEN_SYSTEM_PROMPT);
        return ApiResponse.success(documentation);
    }
}
