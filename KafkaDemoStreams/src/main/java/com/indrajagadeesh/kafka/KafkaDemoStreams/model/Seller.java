package com.indrajagadeesh.kafka.KafkaDemoStreams.model;

import lombok.Data;

@Data
public class Seller implements Base {
    private int id;
    private String name;
    private String location;
}
