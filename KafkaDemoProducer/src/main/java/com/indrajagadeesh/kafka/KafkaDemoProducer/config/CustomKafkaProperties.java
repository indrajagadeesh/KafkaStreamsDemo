package com.indrajagadeesh.kafka.KafkaDemoProducer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("kafka")
public class CustomKafkaProperties {
    private String bootstarpServers;
    private String appId;
    private String schemaRegistryUrl;
}
