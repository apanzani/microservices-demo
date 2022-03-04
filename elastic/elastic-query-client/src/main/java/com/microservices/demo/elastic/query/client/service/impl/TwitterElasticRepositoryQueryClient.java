package com.microservices.demo.elastic.query.client.service.impl;

import com.microservices.demo.common.util.CollectionsUtil;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.exception.ElasticQueryClientException;
import com.microservices.demo.elastic.query.client.repository.TwitterElastisearchQueryRepository;
import com.microservices.demo.elastic.query.client.service.ElasticQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class TwitterElasticRepositoryQueryClient implements ElasticQueryService<TwitterIndexModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticRepositoryQueryClient.class);

    private final TwitterElastisearchQueryRepository twitterElastisearchQueryRepository;

    public TwitterElasticRepositoryQueryClient(TwitterElastisearchQueryRepository twitterElastisearchQueryRepository) {
        this.twitterElastisearchQueryRepository = twitterElastisearchQueryRepository;
    }

    @Override
    public TwitterIndexModel getIndexModelById(String id) {
        Optional<TwitterIndexModel> searchResult = twitterElastisearchQueryRepository.findById(id);
        LOG.info("Document with id {} retrieved successfully", searchResult.orElseThrow(
                () -> new ElasticQueryClientException("No document found at elasticseach with id "
                + id)).getId());
        return searchResult.get();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByText(String text) {
        List<TwitterIndexModel> searchResult = twitterElastisearchQueryRepository.findByText(text);
        LOG.info("{} of document with text {} retrieved successfully", searchResult.size(), text);
        return searchResult;
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        List<TwitterIndexModel> searchResult = CollectionsUtil.getInstance().getListFromIterable(twitterElastisearchQueryRepository.findAll());
        return searchResult;
    }
}
