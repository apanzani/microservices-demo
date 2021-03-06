package com.microservices.demo.elastic.query.service.model.assembler;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.service.api.ElasticDocumentController;
import com.microservices.demo.elastic.query.service.common.model.ElasticQuerySeviceResponseModel;
import com.microservices.demo.elastic.query.service.common.transformer.ElasticToResponseModelTransformer;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ElasticQueryServiceResponseModelAssembler extends RepresentationModelAssemblerSupport<TwitterIndexModel, ElasticQuerySeviceResponseModel> {

    private final ElasticToResponseModelTransformer elasticToResponseModelTransformer;

    public ElasticQueryServiceResponseModelAssembler(ElasticToResponseModelTransformer elasticToResponseModelTransformer) {
        super(ElasticDocumentController.class, ElasticQuerySeviceResponseModel.class);
        this.elasticToResponseModelTransformer = elasticToResponseModelTransformer;
    }

    @Override
    public ElasticQuerySeviceResponseModel toModel(TwitterIndexModel entity) {
        ElasticQuerySeviceResponseModel responseModel = elasticToResponseModelTransformer.getResponseModel(entity);
        responseModel.add(
                linkTo(methodOn(ElasticDocumentController.class)
                        .getDocumentById(entity.getId()))
                        .withSelfRel());
        responseModel.add(
                linkTo(ElasticDocumentController.class)
                        .withRel("documents"));
        return responseModel;
    }

    public List<ElasticQuerySeviceResponseModel> toModels(List<TwitterIndexModel> entities) {
        return entities.stream().map(this::toModel).collect(Collectors.toList());
    }
}
