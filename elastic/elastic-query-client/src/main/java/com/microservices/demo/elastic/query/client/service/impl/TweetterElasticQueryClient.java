package com.microservices.demo.elastic.query.client.service.impl;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.exception.ElasticQueryClientException;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.client.util.ElasticQueryUtil;
import com.microservices.demo.config.ElasticConfigData;
import com.microservices.demo.config.ElasticQueryConfigData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TweetterElasticQueryClient implements ElasticQueryClient<TwitterIndexModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TweetterElasticQueryClient.class);

    private final ElasticConfigData elasticConfigData;
    private final ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil;
    private final ElasticQueryConfigData elasticQueryConfigData;
    private final ElasticsearchOperations elasticsearchOperations;

    public TweetterElasticQueryClient(ElasticConfigData elasticConfigData, ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil, ElasticQueryConfigData elasticQueryConfigData, ElasticsearchOperations elasticsearchOperations) {
        this.elasticConfigData = elasticConfigData;
        this.elasticQueryUtil = elasticQueryUtil;
        this.elasticQueryConfigData = elasticQueryConfigData;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public TwitterIndexModel getIndexModelById(String id) {
        Query searchQueryById = elasticQueryUtil.getSearchQueryById(id);
        SearchHit<TwitterIndexModel> searchHit = elasticsearchOperations.searchOne(searchQueryById, TwitterIndexModel.class,
                IndexCoordinates.of(elasticConfigData.getIndexName()));
        if(searchHit == null){
            LOG.error("No document found at elastisearch with id  {}", id);
            throw new ElasticQueryClientException("No document found at elastisearch with id " + id);
        }
        LOG.info("Documenti with id {} retrieved successfully", searchHit.getId());
        return searchHit.getContent();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByText(String text) {
        Query query = elasticQueryUtil.getSearchQueryByFieldText(elasticQueryConfigData.getTextField(), text);
        return search(query,"{} of document with text {} retrieved successfully", text);
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        Query query = elasticQueryUtil.getSearchQueryForAll();
        return search(query,"{} of document with text {} retrieved successfully");
    }

    private List<TwitterIndexModel> search(Query query, String logMessage, Object... logParams ) {
        SearchHits<TwitterIndexModel> searchResult = elasticsearchOperations.search(query, TwitterIndexModel.class,
                IndexCoordinates.of(elasticConfigData.getIndexName()));
        LOG.info(logMessage, searchResult.getTotalHits(), logParams);
        return searchResult.get().map(SearchHit::getContent).collect(Collectors.toList());
    }
}
