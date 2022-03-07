package com.microservices.demo.elastic.query.service.business;

import com.microservices.demo.elastic.query.service.model.ElasticQuerySeviceResponseModel;

import java.util.List;

public interface ElasticQueryService {

    ElasticQuerySeviceResponseModel getDocumentById(String id);

    List<ElasticQuerySeviceResponseModel> getDocumentByText(String text);

    List<ElasticQuerySeviceResponseModel> getAllDocuments();

}
