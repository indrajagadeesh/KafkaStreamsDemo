package com.indrajagadeesh.kafka.KafkaDemoProducer.config;

import io.opentracing.Tracer;
import io.opentracing.contrib.kafka.TracingProducerInterceptor;
import io.opentracing.util.GlobalTracer;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import java.util.Properties;

@Configuration
public class KafkaProducerConfig {
    @Autowired
    private CustomKafkaProperties properties;

    @Autowired
    Tracer tracer;

    @Bean
    public KafkaProducer getKafkaProducer(){
        GlobalTracer.register(tracer);
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstarpServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                Serdes.String().serializer().getClass().getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, properties.getAppId());
        props.put("schema.registry.url", properties.getSchemaRegistryUrl());
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,
                TracingProducerInterceptor.class.getName());
        return new KafkaProducer(props);
    }

}
