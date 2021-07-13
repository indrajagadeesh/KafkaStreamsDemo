package com.indrajagadeesh.kafka.KafkaDemoProducer.config;

import com.indrajagadeesh.kafka.KafkaDemoProducer.model.Base;
import com.indrajagadeesh.kafka.KafkaDemoProducer.model.Consumer;
import com.indrajagadeesh.kafka.KafkaDemoProducer.model.Product;
import com.indrajagadeesh.kafka.KafkaDemoProducer.model.Seller;
import com.indrajagadeesh.kafka.KafkaDemoProducer.model.Orders;
import com.indrajagadeesh.kafka.KafkaDemoProducer.service.KafkaProduceGenericRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class DataInsertConfig {

    @Autowired
    private CustomKafkaProperties properties;
    @Autowired
    private KafkaProduceGenericRecords<Base> productKafka;

    @PostConstruct
    public void insetData(){
        List<Consumer> consumerList = new ArrayList<>();
        consumerList.add( new Consumer(1001,"user1","location1"));
        consumerList.add( new Consumer(1002,"user2","location2"));
        consumerList.add( new Consumer(1003,"user3","location3"));
        consumerList.forEach( consumer -> productKafka.produceGenericRecords(consumer,properties.getAppId()+"consumer-topic" ));

        List<Product> productList = new ArrayList<>();
        productList.add(new Product(100001,"p1",10.0f,101));
        productList.add(new Product(100002,"p2",12.0f,102));
        productList.add(new Product(100003,"p3",13.0f,101));
        productList.add(new Product(100004,"p4",14.0f,103));
        productList.add(new Product(100005,"p5",15.0f,104));
        productList.add(new Product(100006,"p6",16.0f,102));
        productList.add(new Product(100007,"p7",17.0f,103));
        productList.forEach(product -> productKafka.produceGenericRecords(product , properties.getAppId()+"product-topic"));

        List<Seller> sellerList = new ArrayList<>();
        sellerList.add(new Seller(101,"seller1","location1"));
        sellerList.add(new Seller(102,"seller2","location2"));
        sellerList.add(new Seller(103,"seller3","location3"));
        sellerList.add(new Seller(104,"seller4","location4"));
        sellerList.forEach(seller -> productKafka.produceGenericRecords(seller , properties.getAppId()+"seller-topic"));

        List<Orders> ordersList = new ArrayList<>();
        ordersList.add(new Orders(10000001,1001, Arrays.asList(100001,100003,100007,100004)));
        ordersList.add(new Orders(10000002,1002, Arrays.asList(100002,100003,100005,100004)));
        ordersList.add(new Orders(10000003,1003, Arrays.asList(100007,100006,100002,100005)));
        ordersList.add(new Orders(10000004,1001, Arrays.asList(100004,100003,100002,100001)));
        ordersList.add(new Orders(10000005,1002, Arrays.asList(100001,100002,100006,100005)));
        ordersList.forEach(orders -> productKafka.produceGenericRecords(orders, properties.getAppId()+"orders-topic"));



    }
}
