package io.github.iweidujiang.dsv4.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * 配置属性类
 *
 * 项目名称：deepseek-coding-assistant
 * 创建时间：2026-04-27 23:46
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 */
@Data
@Component
@ConfigurationProperties(prefix = "coding-assistant")
public class CodingAssistantProperties {
    private Api api = new Api();
    private Model model = new Model();
    private Cache cache = new Cache();

    @Data
    public static class Api {
        private int rateLimit = 30;
        private int timeout = 60;
    }

    @Data
    public static class Model {
        private String defaultModel = "deepseek-v4-pro";
        private String fallbackModel = "deepseek-v4-flash";
    }

    @Data
    public static class Cache {
        private int ttl = 3600;
    }
}
