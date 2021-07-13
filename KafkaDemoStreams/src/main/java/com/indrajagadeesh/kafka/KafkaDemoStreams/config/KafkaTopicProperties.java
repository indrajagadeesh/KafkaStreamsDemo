package com.indrajagadeesh.kafka.KafkaDemoStreams.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("kafka.topics")
public class KafkaTopicProperties {
    private String sourceId;
    private String consumerMetircsTopic;
    private String sellerMetircsTopic;
}
