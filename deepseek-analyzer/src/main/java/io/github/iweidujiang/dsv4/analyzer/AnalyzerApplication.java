package io.github.iweidujiang.dsv4.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executors;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * 创建时间：2026-04-29 15:07
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 *
 * 启动类
 */
@SpringBootApplication
public class AnalyzerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnalyzerApplication.class, args);
    }

    /**
     * 开启虚拟线程
     */
    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }
}
