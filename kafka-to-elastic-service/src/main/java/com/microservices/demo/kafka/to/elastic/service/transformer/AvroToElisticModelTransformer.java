package com.microservices.demo.kafka.to.elastic.service.transformer;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AvroToElisticModelTransformer {

    //conversione tra il dato che mi arriva dal Kafka con il dato che voglio inviare a elastic
    public List<TwitterIndexModel> getElasticModels(List<TwitterAvroModel> avroModels) {
        return avroModels.stream().map(
                avroModel -> TwitterIndexModel.builder()
                        .id(String.valueOf(avroModel.getId()))
                        .userId(avroModel.getUserId())
                        .text(avroModel.getText())
                        .createdAt(ZonedDateTime.ofInstant(
                                Instant.ofEpochMilli(avroModel.getCreatedAt()),
                                ZoneId.systemDefault()))
                        .build()
        ).collect(Collectors.toList());
    }
}
