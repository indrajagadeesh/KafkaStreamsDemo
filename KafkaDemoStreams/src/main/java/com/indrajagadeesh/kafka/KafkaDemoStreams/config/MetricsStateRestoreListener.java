package com.indrajagadeesh.kafka.KafkaDemoStreams.config;

import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.streams.processor.StateRestoreListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MetricsStateRestoreListener implements StateRestoreListener {
    private static final Logger LOG = LoggerFactory.getLogger(MetricsStateRestoreListener.class);

    @Override
    public void onRestoreStart(TopicPartition topicPartition, String storeName, long startingOffset, long endingOffset) {
        LOG.info("Starting restore of stateStore: {} from partition: {}", storeName,topicPartition);
    }

    @Override
    public void onBatchRestored(TopicPartition topicPartition, String storeName, long batchEndOffset, long numRestored) {
        //
    }

    @Override
    public void onRestoreEnd(TopicPartition topicPartition, String storeName, long totalRestored) {
        LOG.info("Restoraration complate for stateStore: {} from partition: {}", storeName,topicPartition);
    }
}