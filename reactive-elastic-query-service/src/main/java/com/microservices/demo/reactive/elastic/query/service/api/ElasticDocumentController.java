package com.microservices.demo.reactive.elastic.query.service.api;

import com.microservices.demo.elastic.query.service.common.model.ElasticQuerySeviceRequestModel;
import com.microservices.demo.elastic.query.service.common.model.ElasticQuerySeviceResponseModel;
import com.microservices.demo.reactive.elastic.query.service.business.ElasticQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PrePostAdviceReactiveMethodInterceptor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/documents")
public class ElasticDocumentController {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentController.class);

    private final ElasticQueryService elasticQueryService;

    public ElasticDocumentController(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @PostMapping(value = "/get-doc-by-text")
    public Flux<ElasticQuerySeviceResponseModel> getDocumentByText(
            @RequestBody @Valid ElasticQuerySeviceRequestModel requestModel) {
        Flux<ElasticQuerySeviceResponseModel> response = elasticQueryService.getDocumentByText(requestModel.getText());
        response = response.log();
        LOG.info("Returning from query reactive service for text {}", requestModel.getText());
        return response;
    }

}

