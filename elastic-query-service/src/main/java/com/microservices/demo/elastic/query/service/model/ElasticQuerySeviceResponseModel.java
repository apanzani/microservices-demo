package com.microservices.demo.elastic.query.service.model;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class ElasticQuerySeviceResponseModel extends RepresentationModel<ElasticQuerySeviceResponseModel> {

    private String id;
    private String text;
    private Long userId;
    private ZonedDateTime createdAt;
}
