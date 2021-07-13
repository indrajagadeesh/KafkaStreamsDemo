package com.indrajagadeesh.kafka.KafkaDemoStreams.kafka;

import com.indrajagadeesh.kafka.KafkaDemoStreams.config.KafkaTopicProperties;
import com.indrajagadeesh.kafka.KafkaDemoStreams.config.SerdeConfig;
import com.indrajagadeesh.kafka.KafkaDemoStreams.model.*;
import com.indrajagadeesh.kafka.KafkaDemoStreams.service.GenericRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.reflect.ReflectData;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
//@EnableKafkaStreams
@Component
public class AmountsAndCountMetrics {

    @Autowired
    SerdeConfig serdeConfig;

    @Autowired
    KafkaTopicProperties topicProperties;

    @Autowired
    GenericRecordMapper mapper;

    KeyValueMapper<String, GenericRecord, String> orderKeyValueMapper = (key, value) -> {
        return key;
    };

    ValueJoiner<GenericRecord, GenericRecord, GenericRecord> orderValueJoiner = (value1, value2) -> {
        Orders orders = mapper.mapRecordToObject(value1, new Orders());
        log.info("order : {} ----- consumer: {} ",orders, value2);
        ConsumerOrders record = new ConsumerOrders(orders.getConsumerId(), value2.get("name").toString(), value2.get("location").toString(), orders.getId(), orders.getItemsId());
        return mapper.mapObjectToRecord(record);
    };


    public KStream<String, GenericRecord> metricsAggregator(GlobalKTable<String, GenericRecord> consumerRecords,GlobalKTable<String, GenericRecord> sellerRecords,GlobalKTable<String, GenericRecord> productRecords,KStream<String, GenericRecord> orderRecords, StreamsBuilder streamsBuilder) {

        KStream<String, PCO> OrderConsumerProduct = orderRecords
                .peek((key, value) -> log.info("1  key : {} ---- value : {}", key, value))
                .map((key, value) ->
                        new KeyValue<String, GenericRecord>(value.get("consumerId").toString(), value))
                .peek((key, value) -> log.info("1.1  key : {} ---- value : {}", key, value))
                .leftJoin(consumerRecords, orderKeyValueMapper, orderValueJoiner)
                .peek((key, value) -> log.info("2  key : {} ---- value : {}", key, value))
                .flatMap((key, value) -> {
                    ConsumerOrders record = mapper.mapRecordToObject(value, new ConsumerOrders());
                    List<KeyValue<String, ConsumerOrder>> records = new ArrayList<>();
                    record.getItemsId().forEach(itemId -> records
                            .add(new KeyValue<String, ConsumerOrder>(itemId.toString(), new ConsumerOrder(
                                    record.getConsumerId(),
                                    record.getConsumerName(),
                                    record.getConsumerLocation(),
                                    record.getOrderId(),
                                    itemId))));
                    return records;
                })
                .peek((key, value) -> log.info("3  key : {} ---- value : {}", key, value))
                .leftJoin(productRecords, (key, value) -> key,
                        (co, p) -> {
                    log.info("3.1 CO : {} ------- P : {}",co, p);
                            return new PCO(co.getConsumerId(), co.getConsumerName(),
                                    co.getConsumerLocation(), co.getOrderId(), co.getItemId(),
                                    p.get("name").toString(), Double.valueOf(p.get("price").toString()), Integer.valueOf(p.get("sellerId").toString()));
                        });

        OrderConsumerProduct
                .map((key, value) -> new KeyValue<>(String.valueOf(value.getConsumerId()), mapper.mapObjectToRecord(value)))
                .peek((key, value) -> log.info("4  key : {} ---- value : {}", key, value))
                .groupByKey()
                .aggregate(() -> {
                    final Schema schema = ReflectData.get().getSchema(ConsumerMetrics.class);
                    return new GenericData.Record(schema);
                }, (key, val, agg) -> {
                    log.info("4.1 val : {} ------ agg : {}",val,agg);
                    ConsumerMetrics aggregate = null;
                    if(agg != null)
                         aggregate = mapper.mapRecordToObject(agg, new ConsumerMetrics());
                    PCO value = mapper.mapRecordToObject(val , new PCO());
                    if (aggregate.getName() == null ) {
                        aggregate = new ConsumerMetrics(value.getConsumerId(), value.getConsumerName(), value.getProductPrice(),  1, value.getConsumerLocation());
                    } else {
                        aggregate.setTotalSpend(aggregate.getTotalSpend() + value.getProductPrice());
                        aggregate.setCount(aggregate.getCount()+1);

                    }
                    return (GenericData.Record)mapper.mapObjectToRecord(aggregate);
                })
                .mapValues(value -> (GenericRecord)value)
                .toStream()
                .peek((key, value) -> log.info("5  key : {} ---- value : {}", key, value))
                .to(topicProperties.getConsumerMetircsTopic(),
                        Produced.with(Serdes.String(), serdeConfig.getGenericAvroSerdeValue()));

        return orderRecords;


//        OrderConsumer.map((key, value) -> {
//            ConsumerOrder record = mapper.mapRecordToObject(value, new ConsumerOrder());
//            return new KeyValue<String,ConsumerOrder>(String.valueOf(record.getConsumerId()), record); })
//                .groupByKey()
//                .aggregate()


    }
}
