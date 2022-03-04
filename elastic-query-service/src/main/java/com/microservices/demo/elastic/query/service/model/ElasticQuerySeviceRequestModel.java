package com.microservices.demo.elastic.query.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElasticQuerySeviceRequestModel {

    private String id;
    private Long userId;
    private String text;
    private ZonedDateTime createdAt;
}
