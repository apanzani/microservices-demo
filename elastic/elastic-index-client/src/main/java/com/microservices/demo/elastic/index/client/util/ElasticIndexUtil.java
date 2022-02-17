package com.microservices.demo.elastic.index.client.util;

import com.microservices.demo.elastic.model.index.IndexModel;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/*they will convert the list of Twitter index model objects to a list of index queries
* to be able to send them to ElasticSearch.*/
@Component
public class ElasticIndexUtil<T extends IndexModel> {

    public List<IndexQuery> getIntexQueries(List<T> documents){
        return documents.stream()
                .map(createIndexQuery()
                ).collect(Collectors.toList());
    }

    private Function<T, IndexQuery> createIndexQuery() {
        return document -> new IndexQueryBuilder()
                .withId(document.getId())
                .withObject(document)
                .build();
    }
}
