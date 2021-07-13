package com.indrajagadeesh.kafka.KafkaDemoStreams.config;

import lombok.Cleanup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.CleanupConfig;

import java.util.List;

@Configuration
public class AppConfig {
    private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);
    private final List<String> conditionalLogKeys;
    private final boolean cleanpuOnStart;

    public AppConfig(@Value("${application.cleanupOnStart:false}") boolean cleanpuOnStart,
                     @Value("${application.logkeys}") List<String> conditionalLogKeys){
        this.cleanpuOnStart = cleanpuOnStart;
        this.conditionalLogKeys = conditionalLogKeys;
    }

    @Bean
    public CleanupConfig cleanupConfig(){
        LOG.info("Will cleanup statestore on startup: {}",cleanpuOnStart);
        return new CleanupConfig(cleanpuOnStart, false);
    }

    @Bean
    public List<String> conditionalLogKeys(){
        return conditionalLogKeys;
    }
}