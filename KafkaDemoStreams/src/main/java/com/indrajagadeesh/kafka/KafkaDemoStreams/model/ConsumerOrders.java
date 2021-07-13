package com.indrajagadeesh.kafka.KafkaDemoStreams.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerOrders {
    private int consumerId;
    private String ConsumerName;
    private String ConsumerLocation;
    private int orderId;
    private List<Integer> itemsId;
}
