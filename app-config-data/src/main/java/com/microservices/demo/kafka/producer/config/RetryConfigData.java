package com.microservices.demo.kafka.producer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "retry-config")
public class RetryConfigData {

    private Long initialIntervalMs;
    private Long maxIntervalMs;
    private Double mutiplier;
    private Integer maxAttemps;
    private Long sleepTimeMs;
}
