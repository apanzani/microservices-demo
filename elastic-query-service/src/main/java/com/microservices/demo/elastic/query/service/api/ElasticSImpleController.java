package com.microservices.demo.elastic.query.service.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class ElasticSImpleController {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticSImpleController.class);

    @GetMapping
    public String getAllDocuments() {
        return "Hello";
    }

    @GetMapping("/v1")
    public ResponseEntity<String> getAllDocumentsV1() {
        return ResponseEntity.ok("response");
    }

}
