package com.bheaver.ngl4.esreactor.cataloguing;

import com.bheaver.ngl4.importCatalog.ImportCatalogRecords;
import com.bheaver.ngl4.importCatalog.model.MarcRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class CataloguingHandler {
    @Autowired
    @Qualifier(com.bheaver.ngl4.importCatalog.model.BeanNames.IMPORT_CATALOG_RECORD)
    ImportCatalogRecords importCatalogRecords;
    public Mono<ServerResponse> importCatalogRecord(ServerRequest request){

        return importCatalogRecords.parseRecords(request.formData().map(stringStringMultiValueMap -> stringStringMultiValueMap.getFirst("data"))).collectList().flatMap(marcRecord -> {
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromObject(marcRecord));
        });
    }
}
