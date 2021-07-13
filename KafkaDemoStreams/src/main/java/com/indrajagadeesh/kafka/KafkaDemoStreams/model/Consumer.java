package com.indrajagadeesh.kafka.KafkaDemoStreams.model;

import lombok.Data;

@Data
public class Consumer implements Base {
    private int id;
    private String name;
    private String location;
}
