package com.microservices.demo.elastic.query.service.api;

import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.model.ElasticQuerySeviceRequestModel;
import com.microservices.demo.elastic.query.service.model.ElasticQuerySeviceResponseModel;
import com.microservices.demo.elastic.query.service.model.ElasticQuerySeviceResponseModelV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

// L'annotation restController contiene già di per se ResponseBody e di conseguenza
// converte autometicamente la response in JSON
@RestController
@RequestMapping(value = "/documents", produces = "application/vnd.api.v1+json")
public class ElasticDocumentController {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentController.class);

    private final ElasticQueryService elasticQueryService;

    public ElasticDocumentController(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @Operation(summary = "Get all elastic documents.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response.",
                    content = {@Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticQuerySeviceResponseModel.class))}),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error."),
    })
    @GetMapping
    public @ResponseBody
    ResponseEntity<List<ElasticQuerySeviceResponseModel>> getAllDocuments() {
        List<ElasticQuerySeviceResponseModel> response = elasticQueryService.getAllDocuments();
        LOG.info("Elasticsearch returned {} of documents ", response.size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get document by Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response.",
                    content = {@Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticQuerySeviceResponseModel.class))}),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error."),
    })
    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<ElasticQuerySeviceResponseModel> getDocumentById(
            @PathVariable @NotEmpty String id) {
        ElasticQuerySeviceResponseModel responseModel = elasticQueryService.getDocumentById(id);
        LOG.debug("Elasticsearch returned document with id {} ", id);
        return ResponseEntity.ok(responseModel);
    }

    @Operation(summary = "Get document by Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response.",
                    content = {@Content(mediaType = "application/vnd.api.v2+json",
                            schema = @Schema(implementation = ElasticQuerySeviceResponseModelV2.class))}),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error."),
    })
    @GetMapping(value = "/{id}", produces = "application/vnd.api.v2+json")
    public @ResponseBody
    ResponseEntity<ElasticQuerySeviceResponseModelV2> getDocumentByIdV2(
            @PathVariable @NotEmpty String id) {
        ElasticQuerySeviceResponseModel responseModel = elasticQueryService.getDocumentById(id);
        LOG.debug("Elasticsearch returned document with id {} ", id);
        return ResponseEntity.ok(getV2Model(responseModel));
    }

    @Operation(summary = "Get document by text.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response.",
                    content = {@Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticQuerySeviceResponseModel.class))}),
            @ApiResponse(responseCode = "400", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error."),
    })
    @PostMapping("/get-documet-by-text")
    public @ResponseBody
    ResponseEntity<List<ElasticQuerySeviceResponseModel>> getDocumentByText(
            @RequestBody @Valid ElasticQuerySeviceRequestModel requestModel) {

        List<ElasticQuerySeviceResponseModel> responseModels = elasticQueryService.getDocumentByText(requestModel.getText());
        LOG.info("Elasticsearch returned {} of documents ", responseModels.size());
        return ResponseEntity.ok(responseModels);
    }

    private ElasticQuerySeviceResponseModelV2 getV2Model(ElasticQuerySeviceResponseModel responseModel) {
        return ElasticQuerySeviceResponseModelV2.builder()
                .id(Long.parseLong(responseModel.getId()))
                .userId(responseModel.getUserId())
                .createdAt(responseModel.getCreatedAt())
                .text(responseModel.getText())
                .text2(responseModel.getText())
                .build();
    }
}
