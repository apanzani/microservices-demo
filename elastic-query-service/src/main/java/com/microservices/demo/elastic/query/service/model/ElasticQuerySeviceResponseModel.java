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
public class ElasticQuerySeviceResponseModel extends RepresentationModel<ElasticQuerySeviceResponseModel> {

    private String id;
    private String text;
    private Long userId;
    private ZonedDateTime createdAt;
}
