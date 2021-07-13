package com.indrajagadeesh.kafka.KafkaDemoProducer.service;

import com.indrajagadeesh.kafka.KafkaDemoProducer.config.KafkaProducerConfig;
import com.indrajagadeesh.kafka.KafkaDemoProducer.model.Base;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class KafkaProduceGenericRecords<T extends Base> {

    @Autowired
    KafkaProducerConfig producer;
    @Autowired
    GenericRecordMapper mapper;

    public String produceGenericRecords(T data, String topic){
        long offset = 0L;
        String key = String.valueOf(data.getId());
        GenericRecord value = mapper.mapObjectToRecord(data);
        ProducerRecord<String, GenericRecord> record = new ProducerRecord<>(topic, key, value);
        try {
            Future<RecordMetadata> meta = producer.getKafkaProducer().send(record);
            offset = meta.get(5, TimeUnit.SECONDS).offset();
            log.info("message sent successfully with offset : "+ offset);
        } catch(SerializationException | InterruptedException | ExecutionException | TimeoutException e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
        return "message offset : "+ offset;
    }

}
