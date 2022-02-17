package com.microservices.demo.elastic.index.client.service.impl;

import com.microservices.demo.elastic.index.client.repository.TwitterElasticsearchRepository;
import com.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//siccome ho due Service che implementano la stessa interfaccia devo indicare quale dei 2 è prioritario rispetto
//all'altro.
//@Primary
@Service
//Visto che però usi le configurazioni l'annotation Primary non mi serve più
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "true", matchIfMissing = true)
public class TwitterElasticRepositoryIndexClient implements ElasticIndexClient<TwitterIndexModel> {
    /* As you can see with the repository, it is easier to write Elasticsearch Operation,
       but it has less control than using elastic search operations claas,
       which are also working with a low level analysis, search queries.*/
    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticRepositoryIndexClient.class);

    private final TwitterElasticsearchRepository twitterElasticsearchRepository;

    public TwitterElasticRepositoryIndexClient(TwitterElasticsearchRepository twitterElasticsearchRepository) {
        this.twitterElasticsearchRepository = twitterElasticsearchRepository;
    }

    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<TwitterIndexModel> twitterIndexModels = (List<TwitterIndexModel>) twitterElasticsearchRepository.saveAll(documents);
        List<String> ids = twitterIndexModels.stream().map(TwitterIndexModel::getId).collect(Collectors.toList());
        LOG.info("Documents indexed successfully with type: {} and ids: {}", TwitterIndexModel.class.getName()
                , ids);
        return ids;
    }
}
