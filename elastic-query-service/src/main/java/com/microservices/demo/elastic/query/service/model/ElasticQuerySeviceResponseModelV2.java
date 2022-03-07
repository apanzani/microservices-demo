package com.microservices.demo.elastic.query.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElasticQuerySeviceResponseModelV2 extends RepresentationModel<ElasticQuerySeviceResponseModelV2> {

    private Long id;
    private String text;
    private Long userId;
    private ZonedDateTime createdAt;
}
