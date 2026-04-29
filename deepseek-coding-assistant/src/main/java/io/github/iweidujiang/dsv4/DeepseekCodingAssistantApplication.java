package io.github.iweidujiang.dsv4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executors;

@SpringBootApplication
public class DeepseekCodingAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeepseekCodingAssistantApplication.class, args);
    }

    /**
     * JDK 21 虚拟线程配置
     * 使 Tomcat 使用虚拟线程处理请求，显著提升 I/O 密集型任务的并发能力
     */
    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> {
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }
}
