package com.indrajagadeesh.kafka.KafkaDemoProducer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consumer implements Base {
    private int id;
    private String name;
    private String location;
}
