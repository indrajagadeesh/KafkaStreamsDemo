package com.indrajagadeesh.kafka.KafkaDemoStreams.model;

import lombok.Data;

import java.util.List;

@Data
public class Orders implements Base{
    private int id;
    private Integer consumerId;
    private List<Integer> itemsId;
}
