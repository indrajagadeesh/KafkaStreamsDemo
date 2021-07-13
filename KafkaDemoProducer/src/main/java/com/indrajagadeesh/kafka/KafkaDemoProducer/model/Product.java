package com.indrajagadeesh.kafka.KafkaDemoProducer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Base {
    private int id;
    private String name;
    private double price;
    private int sellerId;
}
