package com.microservices.demo.kafka.to.elastic.service.consumer.impl;

import com.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.kafka.admin.client.KafkaAdminClient;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.KafkaConsumerConfigData;
import com.microservices.demo.kafka.to.elastic.service.consumer.KafkaConsumer;
import com.microservices.demo.kafka.to.elastic.service.transformer.AvroToElisticModelTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
public class TwitterToKafkaConsumer implements KafkaConsumer<Long, TwitterAvroModel> {
    private static final Logger LOG = LoggerFactory.getLogger(TwitterToKafkaConsumer.class);

    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaConsumerConfigData kafkaConsumerConfigData;
    private final AvroToElisticModelTransformer avroToElisticModelTransformer;
    private final ElasticIndexClient<TwitterIndexModel> elasticIndexClient;

    public TwitterToKafkaConsumer(KafkaConfigData kafkaConfigData, KafkaAdminClient kafkaAdminClient, KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry, KafkaConsumerConfigData kafkaConsumerConfigData, AvroToElisticModelTransformer avroToElisticModelTransformer, ElasticIndexClient<TwitterIndexModel> elasticIndexClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaConsumerConfigData = kafkaConsumerConfigData;
        this.avroToElisticModelTransformer = avroToElisticModelTransformer;
        this.elasticIndexClient = elasticIndexClient;
    }

    @EventListener
    public void onAppStarted(ApplicationStartedEvent event){
        kafkaAdminClient.checkTopicsCreated();
        LOG.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopicNamesToCreate().toArray());
        Objects.requireNonNull(kafkaListenerEndpointRegistry.getListenerContainer(kafkaConsumerConfigData.getConsumerGroupId())).start();
    }

    @Override
    //${kafka-config.topic-name} è nel file config-client-kafka_to_elastic.yml
    @KafkaListener(id = "${kafka-consumer-config.consumer-group-id}", topics = "${kafka-config.topic-name}")
    public void receive(@Payload List<TwitterAvroModel> message,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Integer> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        LOG.info("###############################################################################");
        LOG.info("Kafka listener has been woke up and has received {} number of message with keys {}, " +
                        "partitions {} and offsets {} and will be send to elastic: Thread id {}",
                message.size(), keys, partitions.toString(),offsets.toString(), Thread.currentThread().getId());
        if(!CollectionUtils.isEmpty(message)){
            message.forEach(this::logMessage);
        }
        List<TwitterIndexModel> twitterIndexModels = avroToElisticModelTransformer.getElasticModels(message);
        List<String> documentIds = elasticIndexClient.save(twitterIndexModels);
        LOG.info("Documents key received {} saved to elasticsearch with ids: {}",keys, documentIds.toArray() );
        LOG.info("###############################################################################");
    }

    private void logMessage(TwitterAvroModel twitterAvroModel) {
        LOG.info("*************************************************");
        LOG.info("Massage received: {}", twitterAvroModel.getText());
        LOG.info("*************************************************");
    }
}
