package io.github.iweidujiang.dsv4.analyzer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * DeepSeek配置类
 * <p>
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 * <p>
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * <a href="https://github.com/iweidujiang">GitHub</a>
 * <p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "deepseek")
public class DeepSeekProperties {
    private Models models = new Models();
    private Retry retry = new Retry();

    @Data
    public static class Models {
        private String primary = "deepseek-v4-pro";
        private String fallback = "deepseek-v4-flash";
    }

    @Data
    public static class Retry {
        private int maxAttempts = 2;
        private long backoffMillis = 1000;
    }
}
