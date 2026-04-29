package io.github.iweidujiang.dsv4.controller;

import io.github.iweidujiang.dsv4.commom.ApiResponse;
import io.github.iweidujiang.dsv4.dto.CodeGenerateRequest;
import io.github.iweidujiang.dsv4.dto.CodeGenerateResponse;
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
 * 智能代码生成
 *
 * 项目名称：deepseek-coding-assistant
 * 创建时间：2026-04-27 23:56
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/code")
public class CodeGenerationController {
    private final DeepSeekService deepSeekService;

    private static final String SYSTEM_PROMPT_CODE_GENERATION = """
            你是一位顶尖的软件架构师和资深全栈工程师，精通多种编程语言和设计模式。
            
            代码生成规范:
            1. 只输出纯代码，不要包含任何解释性文字
            2. 代码必须包含完整的import语句和包声明
            3. 遵循对应语言的最佳实践（Java: 遵循Java编码规范）
            4. 包含必要的注释（核心逻辑、边界条件处理）
            5. 预设异常处理和输入验证
            6. 输出格式: 直接输出代码，并用 ```language 标记代码块
            """;

    /**
     * 代码生成接口
     * POST /api/code/generate
     */
    @PostMapping("/generate")
    public ApiResponse<CodeGenerateResponse> generateCode(@Valid @RequestBody CodeGenerateRequest request) {
        log.info("代码生成请求 - 语言: {}, 需求: {}", request.getLanguage(), request.getRequirement());

        String prompt = buildCodeGenerationPrompt(request);
        String response = deepSeekService.generateWithSystem(prompt, SYSTEM_PROMPT_CODE_GENERATION);

        CodeGenerateResponse result = CodeGenerateResponse.builder()
                .code(extractCodeFromResponse(response))
                .explanation(request.getNeedExplanation() ? extractExplanation(response) : null)
                .language(request.getLanguage())
                .tokensUsed(estimateTokens(response))
                .build();

        return ApiResponse.success(result);
    }

    /**
     * 构建代码生成Prompt
     */
    private String buildCodeGenerationPrompt(CodeGenerateRequest request) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("请生成一段").append(request.getLanguage().toUpperCase()).append("代码。\n");
        prompt.append("功能需求: ").append(request.getRequirement()).append("\n");

        if (!"none".equals(request.getFramework())) {
            prompt.append("技术框架: ").append(request.getFramework()).append("\n");
        }

        prompt.append("\n要求:\n");
        prompt.append("- 代码可直接运行\n");
        prompt.append("- 包含包声明和导入语句\n");
        prompt.append("- 类、方法设计合理\n");
        prompt.append("- 添加必要的注释\n");

        return prompt.toString();
    }

    private String extractCodeFromResponse(String response) {
        // 提取markdown代码块中的代码
        if (response == null) return "";
        int start = response.indexOf("```");
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

    private String extractExplanation(String response) {
        // 提取代码块以外的解释部分
        if (response == null) return "";
        int firstCodeBlock = response.indexOf("```");
        if (firstCodeBlock == -1) return response;
        return response.substring(0, firstCodeBlock).trim();
    }

    private long estimateTokens(String text) {
        // 粗略估算：中文约2字符/token，英文约4字符/token
        if (text == null) return 0;
        return (long) (text.length() / 2.5);
    }
}
