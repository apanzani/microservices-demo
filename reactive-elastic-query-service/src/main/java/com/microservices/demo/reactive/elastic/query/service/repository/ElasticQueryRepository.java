package com.microservices.demo.reactive.elastic.query.service.repository;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ElasticQueryRepository extends ReactiveCrudRepository<TwitterIndexModel, String> {
    // Mono e Flux sono due elementi di Reac
    //Mono and Flux API types to work on data sequences of 0..1 (Mono) and 0..N (Flux)
    Flux<TwitterIndexModel> findByText(String text);
}
