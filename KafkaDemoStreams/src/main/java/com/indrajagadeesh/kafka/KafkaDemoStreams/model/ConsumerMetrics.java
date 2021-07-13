package com.indrajagadeesh.kafka.KafkaDemoStreams.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerMetrics {
    private Integer id;
    private String name;
    private Double totalSpend;
    private Integer count;
    private String location;
}
