package com.microservices.demo.elastic.query.service.api;

import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.model.ElasticQuerySeviceRequestModel;
import com.microservices.demo.elastic.query.service.model.ElasticQuerySeviceResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/documents")
public class ElasticDocumentController {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentController.class);

    private final ElasticQueryService elasticQueryService;

    public ElasticDocumentController(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity<List<ElasticQuerySeviceResponseModel>> getAllDocuments(){
        List<ElasticQuerySeviceResponseModel> response = elasticQueryService.getAllDocuments();
        LOG.info("Elasticsearch returned {} of documents ", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<ElasticQuerySeviceResponseModel> getDocumentById(
            @PathVariable @NotEmpty String id){
        ElasticQuerySeviceResponseModel responseModel = elasticQueryService.getDocumentById(id);
        LOG.debug("Elasticsearch returned document with id {} ", id);
        return ResponseEntity.ok(responseModel);
    }

    @PostMapping("/get-documet-by-text")
    public @ResponseBody ResponseEntity<List<ElasticQuerySeviceResponseModel>> getDocumentByText(
            @RequestBody @Valid ElasticQuerySeviceRequestModel requestModel){

        List<ElasticQuerySeviceResponseModel> responseModels = elasticQueryService.getDocumentByText(requestModel.getText());
        LOG.info("Elasticsearch returned {} of documents ", responseModels.size());
        return ResponseEntity.ok(responseModels);
    }
}
