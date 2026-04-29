package io.github.iweidujiang.dsv4.function;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * Tool Calls：让AI调用外部工具
 *
 * 项目名称：deepseek-coding-assistant
 * 创建时间：2026-04-27 23:15
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 */
@Slf4j
@Configuration
public class ToolCallConfig {
    @Bean
    public Function<WeatherRequest, WeatherResponse> currentWeather() {
        return request -> {
            log.info("调用天气API - 城市: {}", request.location());
            // 调用真实天气API
            return new WeatherResponse(request.location(), "24°C", "晴");
        };
    }

    public record WeatherRequest(String location) {}

    public record WeatherResponse(String location, String temperature, String condition) {}
}
