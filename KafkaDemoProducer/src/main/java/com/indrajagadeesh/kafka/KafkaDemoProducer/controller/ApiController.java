package com.indrajagadeesh.kafka.KafkaDemoProducer.controller;

import com.indrajagadeesh.kafka.KafkaDemoProducer.config.CustomKafkaProperties;
import com.indrajagadeesh.kafka.KafkaDemoProducer.model.*;
import com.indrajagadeesh.kafka.KafkaDemoProducer.service.KafkaProduceGenericRecords;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produce")
public class ApiController {

    @Autowired
    private CustomKafkaProperties properties;
    @Autowired
    private KafkaProduceGenericRecords<Base> productKafka;

    @PostMapping("/consumer")
    public String produceConsumer(@RequestBody Consumer consumer){
        return productKafka.produceGenericRecords(consumer , properties.getAppId()+"consumer-topic");
    }

    @PostMapping("/product")
    public String produceProduct(@RequestBody Product product){
        return productKafka.produceGenericRecords(product , properties.getAppId()+"product-topic");
    }

    @PostMapping("/seller")
    public String produceSeller(@RequestBody Seller seller){
        return productKafka.produceGenericRecords(seller , properties.getAppId()+"seller-topic");
    }

    @PostMapping("/orders")
    public String produceSells(@RequestBody Orders orders){
        return productKafka.produceGenericRecords(orders, properties.getAppId()+"orders-topic");
    }
}
