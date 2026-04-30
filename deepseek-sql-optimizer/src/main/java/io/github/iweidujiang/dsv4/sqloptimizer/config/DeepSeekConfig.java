package io.github.iweidujiang.dsv4.sqloptimizer.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * DeepSeek 配置类
 * <p>
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 * <p>
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * <a href="https://github.com/iweidujiang">GitHub</a>
 * <p>
 */
@Configuration
public class DeepSeekConfig {

    /**
     * 配置ChatMemory - 使用滑动窗口记忆防止Token爆栈
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                // 使用内存存储（生产环境可替换为RedisChatMemoryRepository）
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                // 保留最近20条消息，防止上下文过长
                .maxMessages(20)
                .build();
    }

    /**
     * 配置带记忆功能的ChatClient
     */
    @Bean
    @Primary
    public ChatClient chatClientWithMemory(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    /**
     * 配置不带记忆的基础版ChatClient（用于代码生成等无状态场景）
     */
    @Bean
    public ChatClient basicChatClient(ChatClient.Builder builder) {
        return builder.build();
    }

}
