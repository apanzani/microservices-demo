package com.microservices.demo.elastic.query.service.business.impl;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.model.ElasticQuerySeviceResponseModel;
import com.microservices.demo.elastic.query.service.model.assembler.ElasticQueryServiceResponseModelAssembler;
import com.microservices.demo.elastic.query.service.transformer.ElasticToResponseModelTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwitterElasticQueryService implements ElasticQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryService.class);

    private final ElasticQueryServiceResponseModelAssembler elasticToResponseModelTransformer;
    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;

    public TwitterElasticQueryService(ElasticQueryServiceResponseModelAssembler elasticToResponseModelTransformer, ElasticQueryClient<TwitterIndexModel> elasticQueryClient) {
        this.elasticToResponseModelTransformer = elasticToResponseModelTransformer;
        this.elasticQueryClient = elasticQueryClient;
    }

    @Override
    public ElasticQuerySeviceResponseModel getDocumentById(String id) {
        LOG.info("Querying elasticsearch by id {}" , id);
        return elasticToResponseModelTransformer.toModel(elasticQueryClient.getIndexModelById(id));
    }

    @Override
    public List<ElasticQuerySeviceResponseModel> getDocumentByText(String text) {
        LOG.info("Querying elasticsearch by text {}" , text);
        return elasticToResponseModelTransformer.toModels(elasticQueryClient.getIndexModelByText(text));
    }

    @Override
    public List<ElasticQuerySeviceResponseModel> getAllDocuments() {
        LOG.info("Querying all documents in elasticsearch");
        return elasticToResponseModelTransformer.toModels(elasticQueryClient.getAllIndexModels());
    }
}
