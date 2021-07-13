package com.indrajagadeesh.kafka.KafkaDemoStreams.config;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SerdeConfig {

    @Value("${spring.kafka.streams.properties.schema.registry.url:http://localhost:8081}")
    private String schemaRegistryUrl;

    public Serde<GenericRecord> getGenericAvroSerdeKey(){
        Serde<GenericRecord> genericAvroSerde = new GenericAvroSerde();
        boolean isKeySerde = true;
        genericAvroSerde.configure(
                Collections.singletonMap(
                        "schema.registry.url",
                        schemaRegistryUrl),
                isKeySerde);
        return genericAvroSerde;
    }

    public Serde<GenericRecord> getGenericAvroSerdeValue(){
        Serde<GenericRecord> genericAvroSerde = new GenericAvroSerde();
        boolean isKeySerde = false;
        genericAvroSerde.configure(
                Collections.singletonMap(
                        "schema.registry.url",
                        schemaRegistryUrl),
                isKeySerde);
        return genericAvroSerde;
    }
}
