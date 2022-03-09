package com.microservices.demo.reactive.elastic.query.service.business;

import com.microservices.demo.elastic.query.service.common.model.ElasticQuerySeviceResponseModel;
import reactor.core.publisher.Flux;

public interface ElasticQueryService {

    Flux<ElasticQuerySeviceResponseModel> getDocumentByText(String text);
}
