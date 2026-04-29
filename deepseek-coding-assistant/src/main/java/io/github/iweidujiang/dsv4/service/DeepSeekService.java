package io.github.iweidujiang.dsv4.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.iweidujiang.dsv4.config.CodingAssistantProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * DeepSeek服务层
 *
 * 项目名称：deepseek-coding-assistant
 * 创建时间：2026-04-27 22:43
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeepSeekService {
    private final ChatClient basicChatClient;

    private final CodingAssistantProperties properties;

    private final ObjectMapper objectMapper;

    /**
     * 同步调用 - 适合快速处理
     */
    public String generateSync(String userMessage) {
        log.info("DeepSeek V4 同步调用: {}", truncateMessage(userMessage));

        try {
            String response = basicChatClient.prompt()
                    .user(userMessage)
                    .options(OpenAiChatOptions.builder()
                            .model(properties.getModel().getDefaultModel())
                            .temperature(0.2)
                            .maxTokens(4096)
                            .build())
                    .call()
                    .content();

            log.info("DeepSeek V4 响应完成，长度: {} 字符", response.length());
            return response;

        } catch (Exception e) {
            log.error("DeepSeek API调用失败: {}", e.getMessage());
            throw new RuntimeException("AI服务暂时不可用，请稍后重试", e);
        }
    }

    /**
     * 带系统提示的同步调用
     */
    public String generateWithSystem(String userMessage, String systemPrompt) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPrompt);
        Prompt prompt = new Prompt(systemPromptTemplate.createMessage(Map.of()),
                OpenAiChatOptions.builder()
                        .model(properties.getModel().getDefaultModel())
                        .temperature(0.3)
                        .build());

        return basicChatClient.prompt(prompt)
                .user(userMessage)
                .call()
                .content();
    }

    /**
     * 流式调用 - 适合实时对话场景
     * 利用 Flux 实现 token 级别输出，能给用户即时反馈
     */
    public Flux<String> generateStream(String userMessage) {
        log.info("DeepSeek V4 流式调用开始: {}", truncateMessage(userMessage));

        return basicChatClient.prompt()
                .user(userMessage)
                .options(OpenAiChatOptions.builder()
                        .model(properties.getModel().getDefaultModel())
                        .temperature(0.3)
                        .maxTokens(4096)
                        .streamUsage(true)
                        .build())
                .stream()
                .content()
                .doOnComplete(() -> log.info("流式调用完成"))
                .doOnError(error -> log.error("流式调用出错: {}", error.getMessage()));
    }

    /**
     * 结构化输出请求 - 利用 JSON Mode 让模型输出固定格式
     */
    public <T> T generateStructured(String userMessage, Class<T> outputClass) {
        String jsonSchema = generateJsonSchema(outputClass);
        String fullPrompt = String.format(
                "请按照以下JSON格式返回结果，只返回JSON，不要有其他内容。\nJSON Schema: %s\n\n需求: %s",
                jsonSchema, userMessage
        );

        String response = generateSync(fullPrompt);
        try {
            return objectMapper.readValue(extractJson(response), outputClass);
        } catch (Exception e) {
            log.error("JSON解析失败: {}", e.getMessage());
            throw new RuntimeException("AI响应格式解析失败", e);
        }
    }

    /**
     * 多轮对话支持（含会话记忆）
     */
    public Flux<ChatResponse> chatWithMemory(String sessionId, String userMessage) {
        // 注意：需要配合 MessageChatMemoryAdvisor 使用
        // 具体配置见后面的配置类
        return basicChatClient.prompt()
                .user(userMessage)
                .advisors(advisor -> advisor
                        .param("conversation_id", sessionId))
                .stream()
                .chatResponse();
    }

    private String truncateMessage(String message) {
        if (message == null) return "";
        return message.length() > 100 ? message.substring(0, 100) + "..." : message;
    }

    private String generateJsonSchema(Class<?> clazz) {
        // 简化实现，实际可用 Jackson 的 Schema 生成
        return "{\"type\": \"object\", \"properties\": {...}}";
    }

    private String extractJson(String response) {
        // 从响应中提取 JSON 部分（处理 markdown 代码块）
        if (response == null) return "{}";
        int start = response.indexOf('{');
        int end = response.lastIndexOf('}');
        if (start != -1 && end != -1 && end > start) {
            return response.substring(start, end + 1);
        }
        return response;
    }
}
