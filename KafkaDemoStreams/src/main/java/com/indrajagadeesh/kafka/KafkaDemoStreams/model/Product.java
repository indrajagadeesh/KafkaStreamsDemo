package com.indrajagadeesh.kafka.KafkaDemoStreams.model;

import lombok.Data;

@Data
public class Product implements Base {
    private int id;
    private String name;
    private double price;
    private int sellerId;
}
