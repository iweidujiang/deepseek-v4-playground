package io.github.iweidujiang.dsv4.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * 监控埋点
 *
 * 项目名称：deepseek-coding-assistant
 * 创建时间：2026-04-27 23:20
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 */
@Slf4j
@Component
public class DeepSeekMetrics {
    private final MeterRegistry meterRegistry;
    private final Counter requestCounter;
    private final DistributionSummary tokenSummary;

    public DeepSeekMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.requestCounter = Counter.builder("deepseek.requests.total")
                .description("Total DeepSeek API requests")
                .register(meterRegistry);
        this.tokenSummary = DistributionSummary.builder("deepseek.tokens.used")
                .description("Tokens used per request")
                .register(meterRegistry);
    }

    public void recordRequest(String model, String operation, boolean success, long tokensUsed) {
        requestCounter.increment();
        tokenSummary.record(tokensUsed);
        log.info("DeepSeek调用 - 模型: {}, 操作: {}, 成功: {}, Tokens: {}",
                model, operation, success, tokensUsed);
    }
}
