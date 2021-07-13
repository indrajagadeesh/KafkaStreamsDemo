package com.indrajagadeesh.kafka.KafkaDemoStreams.config;

import com.indrajagadeesh.kafka.KafkaDemoStreams.kafka.AmountsAndCountMetrics;
import io.opentracing.Tracer;
import io.opentracing.contrib.kafka.streams.TracingKafkaClientSupplier;
import io.opentracing.util.GlobalTracer;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaClientSupplier;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.CleanupConfig;
import java.time.Duration;

import static org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_BUILDER_BEAN_NAME;
import static org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME;


@Slf4j

@Configuration
public class KafkaStreamConfig {

    @Autowired
    private MetricsStateRestoreListener stateRestoreListener;
    @Autowired
    private MetricsStateListener stateListener;
    @Autowired
    AmountsAndCountMetrics metrics;
    @Autowired
    SerdeConfig serdeConfig;
    @Autowired
    KafkaTopicProperties topicProperties;
//    @Autowired
//    Tracer tracer;

    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamConfig.class);
    @Bean(name = DEFAULT_STREAMS_BUILDER_BEAN_NAME)
    public StreamsBuilderFactoryBean defaultKafkaStreamBuilder(
            @Qualifier(DEFAULT_STREAMS_CONFIG_BEAN_NAME) ObjectProvider<KafkaStreamsConfiguration> streamsConfigProvider,
            CleanupConfig cleanupConfig){
        KafkaStreamsConfiguration streamsConfig = streamsConfigProvider.getIfAvailable();
        if(streamsConfig != null){
//            KafkaClientSupplier supplier = new TracingKafkaClientSupplier(tracer);
            StreamsBuilderFactoryBean factoryBean = new StreamsBuilderFactoryBean(streamsConfig, cleanupConfig);
            factoryBean.setStateRestoreListener(stateRestoreListener);
            factoryBean.setStateListener(stateListener);
            factoryBean.setUncaughtExceptionHandler((t,e) -> {
                LOG.error("Unhandled exception, closing KafkaStreams and cleanup local state stores", e);
                factoryBean.getKafkaStreams().close(Duration.ofSeconds(10));
                factoryBean.getKafkaStreams().cleanUp();
            });
            return factoryBean;
        } else {
            throw  new UnsatisfiedDependencyException(KafkaStreamConfig.class.getName(),
                    DEFAULT_STREAMS_BUILDER_BEAN_NAME, "streamConfig","There is no '" +
                    DEFAULT_STREAMS_CONFIG_BEAN_NAME + "' "+ KafkaStreamsConfiguration.class.getName() +
                    " bean in the application context");
        }
    }

    @Bean
    public KStream<?, ?> kStream(StreamsBuilder streamsBuilder){
//        GlobalTracer.register(tracer);
        GlobalKTable<String, GenericRecord> consumerRecords = streamsBuilder.globalTable(topicProperties.getSourceId() + "consumer-topic",
                Consumed.with(Serdes.String(),
                        serdeConfig.getGenericAvroSerdeValue()));

        GlobalKTable<String, GenericRecord> sellerRecords = streamsBuilder.globalTable(topicProperties.getSourceId() + "seller-topic",
                Consumed.with(Serdes.String(),
                        serdeConfig.getGenericAvroSerdeValue()));

        GlobalKTable<String, GenericRecord> productRecords = streamsBuilder.globalTable(topicProperties.getSourceId() + "product-topic",
                Consumed.with(Serdes.String(),
                        serdeConfig.getGenericAvroSerdeValue()));

        KStream<String, GenericRecord> orderRecords = streamsBuilder.stream(topicProperties.getSourceId() + "orders-topic",
                Consumed.with(Serdes.String(),
                        serdeConfig.getGenericAvroSerdeValue()));
        return metrics.metricsAggregator(consumerRecords, sellerRecords, productRecords, orderRecords , streamsBuilder);
    }

}
