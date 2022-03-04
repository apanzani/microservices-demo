package com.microservices.demo.elastic.query.service.api;

import com.microservices.demo.elastic.query.service.model.ElasticQuerySeviceRequestModel;
import com.microservices.demo.elastic.query.service.model.ElasticQuerySeviceResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/documents")
public class ElasticDocumentController {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentController.class);

    @GetMapping("/")
    public @ResponseBody
    ResponseEntity<List<ElasticQuerySeviceResponseModel>> getAllDocuments(){
        List<ElasticQuerySeviceResponseModel> response = new ArrayList<>();
        LOG.info("Elasticsearch returned {} of documents ", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<ElasticQuerySeviceResponseModel> getDocumentById(
            @PathVariable String id){
        ElasticQuerySeviceResponseModel responseModel = ElasticQuerySeviceResponseModel.builder().id(id).build();
        LOG.debug("Elasticsearch returned document with id {} ", id);
        return ResponseEntity.ok(responseModel);
    }

    @PostMapping("/get-documet-by-text")
    public @ResponseBody ResponseEntity<List<ElasticQuerySeviceResponseModel>> getDocumentByText(
            @RequestBody ElasticQuerySeviceRequestModel requestModel){
        ElasticQuerySeviceResponseModel responseModel = ElasticQuerySeviceResponseModel.builder().text(requestModel.getText()).build();

        List<ElasticQuerySeviceResponseModel> responseModels = List.of(responseModel);
        LOG.info("Elasticsearch returned {} of documents ", responseModels.size());
        return ResponseEntity.ok(responseModels);
    }
}
