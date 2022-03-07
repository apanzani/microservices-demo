package com.microservices.demo.elastic.query.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElasticQuerySeviceResponseModelV2 extends RepresentationModel<ElasticQuerySeviceResponseModelV2> {
    //Se aggiungevo solo il campo String text2 non avevo bisogno di cambiare versione perchè per la natura di JSON
    //il client dovrebbe risciare a fare il parse dell'oggetto ignorando il campo in più.
    //Siccome ho cambiato il tipo del campo ID ho bisogno di una nuova versione, perchè introduce una breaking change
    private Long id;
    private String text;
    private String text2;
    private Long userId;
    private ZonedDateTime createdAt;
}
