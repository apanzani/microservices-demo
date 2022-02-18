package com.microservices.demo.elastic.model.index.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microservices.demo.elastic.model.index.IndexModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

// Applied at the class level to indicate this class is a candidate for mapping to the database.
// The most important attributes are:
// indexName: the name of the index to store this entity in. This can contain a SpEL template expression
// like "log-#{T(java.time.LocalDate).now().toString()}"
// https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#reference
// @ means you need to provide a bean with the given name.
@Document(indexName = "#{@elasticConfigData.indexName}")
@Builder
@Data
public class TwitterIndexModel implements IndexModel {

    @JsonProperty
    private String id;
    @JsonProperty
    private Long userId;
    @JsonProperty
    private String text;

    /*Applied at the field level and defines properties of the field, most of the attributes map to the respective Elasticsearch Mapping definitions*/
    /*The pattern attribute can be used to add additional custom format strings. If you want to use only custom date formats, you must set the format property to empty {}*/
    @Field (type = FieldType.Date, format = {}, pattern ="uuuu-MM-dd'T'HH:mm:ssZZ" )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd'T'HH:mm:ssZZ")
    @JsonProperty
    private ZonedDateTime createdAt;

    //posso rimuovere il metodo getId perchè l'annotazione Data mi crea lei il metodo
}
