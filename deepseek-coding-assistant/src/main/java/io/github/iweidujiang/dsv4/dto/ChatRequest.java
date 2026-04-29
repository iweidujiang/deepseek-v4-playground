package io.github.iweidujiang.dsv4.dto;

import lombok.Data;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * 多轮对话请求
 *
 * 项目名称：deepseek-coding-assistant
 * 创建时间：2026-04-27 22:42
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 */
@Data
public class ChatRequest {
    private String sessionId;       // 会话ID，用于多轮记忆
    private String message;
    private String context;         // 附加上下文
}
