package com.microservices.demo.elastic.query.web.client.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElasticQueryWebClientResponseModel {

    private String id;
    private String text;
    private Long userId;
    private ZonedDateTime createdAt;

}
