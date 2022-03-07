package com.microservices.demo.elastic.query.service.transformer;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.service.model.ElasticQuerySeviceResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticToResponseModelTransformer {

    public ElasticQuerySeviceResponseModel getResponseModel(TwitterIndexModel twitterIndexModel){
        return ElasticQuerySeviceResponseModel.builder()
                .id(twitterIndexModel.getId())
                .text(twitterIndexModel.getText())
                .userId(twitterIndexModel.getUserId())
                .createdAt(twitterIndexModel.getCreatedAt())
                .build();
    }

    public List<ElasticQuerySeviceResponseModel> getResponseModels(List<TwitterIndexModel> twitterIndexModels){
        return twitterIndexModels.stream().map(this::getResponseModel).collect(Collectors.toList());
    }
}
