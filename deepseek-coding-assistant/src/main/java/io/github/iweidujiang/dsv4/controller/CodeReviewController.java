package io.github.iweidujiang.dsv4.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.iweidujiang.dsv4.commom.ApiResponse;
import io.github.iweidujiang.dsv4.dto.CodeReviewRequest;
import io.github.iweidujiang.dsv4.dto.CodeReviewResponse;
import io.github.iweidujiang.dsv4.service.DeepSeekService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * 代码审查与优化建议
 *
 * 项目名称：deepseek-coding-assistant
 * 创建时间：2026-04-28 02:57
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/review")
public class CodeReviewController {
    private final DeepSeekService deepSeekService;
    private final ObjectMapper objectMapper;

    private static final String SYSTEM_PROMPT_REVIEW = """
            你是一位严谨的代码审查专家，请按以下维度对代码进行全面审查:
            
            审查维度:
            1. 安全性 (SQL注入、XSS、敏感信息泄露)
            2. 性能 (时间复杂度、资源泄漏、N+1查询)
            3. 可读性 (命名规范、代码复杂度、注释)
            4. 潜在Bug (空指针、边界条件、并发问题)
            5. 最佳实践 (设计模式、SOLID原则)
            
            输出格式要求(JSON):
            {
              "overallScore": 85,
              "criticalIssues": [{"type": "security", "line": 12, "description": "", "suggestion": ""}],
              "warnings": [{"type": "performance", "line": 34, "description": "", "suggestion": ""}],
              "suggestions": ["优化建议1", "优化建议2"],
              "fixedCode": "优化后的代码"
            }
            """;

    @PostMapping("/review")
    public ApiResponse<CodeReviewResponse> reviewCode(@Valid @RequestBody CodeReviewRequest request) {
        log.info("代码审查请求 - 语言: {}, 代码长度: {}",
                request.getLanguage(), request.getCode().length());
        String prompt = String.format("""
                请审查以下%s代码:
                
                ```%s
                %s
                请严格按照JSON格式返回审查结果。
                """, request.getLanguage(), request.getLanguage(), request.getCode());
                
        String response = deepSeekService.generateWithSystem(prompt, SYSTEM_PROMPT_REVIEW);
        CodeReviewResponse reviewResult = parseReviewResponse(response);

        log.info("代码审查完成 - 综合评分: {}, 严重问题数: {}",
                reviewResult.getOverallScore(), reviewResult.getCriticalIssues().size());

        return ApiResponse.success(reviewResult);
    }

    private CodeReviewResponse parseReviewResponse(String response) {
        try {
            // 提取JSON部分
            return objectMapper.readValue(extractJson(response), CodeReviewResponse.class);
        } catch (Exception e) {
            log.error("解析审查结果失败: {}", e.getMessage());
            return CodeReviewResponse.builder()
                    .overallScore(60)
                    .criticalIssues(new ArrayList<>())
                    .warnings(new ArrayList<>())
                    .suggestions(List.of("审查结果解析失败，请重新尝试"))
                    .build();
        }
    }

    private String extractJson(String response) {
        int start = response.indexOf('{');
        int end = response.lastIndexOf('}');
        if (start != -1 && end != -1 && end > start) {
            return response.substring(start, end + 1);
        }
        return "{}";
    }
}
