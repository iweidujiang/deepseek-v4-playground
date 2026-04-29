package io.github.iweidujiang.dsv4.controller;

import io.github.iweidujiang.dsv4.commom.ApiResponse;
import io.github.iweidujiang.dsv4.dto.TestGenerateRequest;
import io.github.iweidujiang.dsv4.dto.TestGenerateResponse;
import io.github.iweidujiang.dsv4.service.DeepSeekService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * 单元测试自动生成
 *
 * 项目名称：deepseek-coding-assistant
 * 创建时间：2026-04-27 23:05
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class UnitTestGenerationController {
    private final DeepSeekService deepSeekService;

    private static final String SYSTEM_PROMPT_TEST_GEN = """
            你是一位测试工程师专家，请为给定的Java代码生成完整的JUnit 5单元测试。
            
            测试规范:
            1. 使用JUnit 5 (Jupiter)
            2. 包含核心测试用例: 正常场景、边界条件、异常场景
            3. 使用@DisplayName提供中文描述
            4. 使用AssertJ或Hamcrest进行断言
            5. 使用Mockito对依赖进行mock
            6. 输出完整可运行的测试代码
            """;

    @PostMapping("/generate")
    public ApiResponse<TestGenerateResponse> generateTest(@Valid @RequestBody TestGenerateRequest request) {
        log.info("单元测试生成请求 - 方法名: {}, 代码长度: {}",
                request.getMethodName(), request.getCode().length());
        String prompt = String.format("""
                为目标方法生成JUnit 5单元测试:
                
                包含以下内容的原始代码:
                ```java
                %s
                目标方法签名: %s %s(%s)
                
                            请输出完整的测试代码。
                """,
                request.getCode(),
                request.getReturnType() != null ? request.getReturnType() : "void",
                request.getMethodName(),
                String.join(", ", request.getParameterTypes()));
        String testCode = deepSeekService.generateWithSystem(prompt, SYSTEM_PROMPT_TEST_GEN);
        String extractedCode = extractTestCode(testCode);

        TestGenerateResponse response = TestGenerateResponse.builder()
                .testCode(extractedCode)
                .framework("JUnit 5")
                .mockitoUsed(detectMockitoUsage(extractedCode))
                .build();

        return ApiResponse.success(response);
    }

    private String extractTestCode(String response) {
        return extractCodeFromResponse(response);
    }

    private String extractCodeFromResponse(String response) {
        if (response == null) return "";
        int start = response.indexOf("```java");
        if (start == -1) start = response.indexOf("```");
        if (start == -1) return response;
        int end = response.indexOf("```", start + 3);
        if (end == -1) return response.substring(start + 3);
        String code = response.substring(start + 3, end);
        int firstNewline = code.indexOf('\n');
        if (firstNewline != -1) {
            code = code.substring(firstNewline + 1);
        }
        return code.trim();
    }

    private boolean detectMockitoUsage(String code) {
        return code.contains("Mockito.") || code.contains("@Mock") || code.contains("@InjectMocks");
    }
}
