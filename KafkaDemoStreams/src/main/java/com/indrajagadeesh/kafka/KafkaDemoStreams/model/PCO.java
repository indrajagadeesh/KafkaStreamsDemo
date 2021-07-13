package com.indrajagadeesh.kafka.KafkaDemoStreams.model;

import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerializableSchema
public class PCO {
    private int consumerId;
    private String consumerName;
    private String consumerLocation;
    private int orderId;
    private Integer itemId;
    private String productName;
    private double productPrice;
    private int sellerId;
}
