package com.indrajagadeesh.kafka.KafkaDemoProducer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Base{
    private int id;
    private Integer consumerId;
    private List<Integer> itemsId;
}
