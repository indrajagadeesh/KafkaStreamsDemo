package com.indrajagadeesh.kafka.KafkaDemoStreams.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerOrder {
    private int consumerId;
    private String ConsumerName;
    private String ConsumerLocation;
    private int orderId;
    private Integer itemId;
}
