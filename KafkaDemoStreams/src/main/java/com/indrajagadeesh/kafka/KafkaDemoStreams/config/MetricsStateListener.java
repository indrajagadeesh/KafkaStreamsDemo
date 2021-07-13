package com.indrajagadeesh.kafka.KafkaDemoStreams.config;

import org.apache.kafka.streams.KafkaStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MetricsStateListener implements KafkaStreams.StateListener {
    private static final Logger LOG = LoggerFactory.getLogger(MetricsStateListener.class);

    @Override
    public void onChange(KafkaStreams.State newState, KafkaStreams.State oldState) {
        LOG.info("stateStore changed for oldState: {} to newState {}",oldState.name(),newState.name());
    }
}