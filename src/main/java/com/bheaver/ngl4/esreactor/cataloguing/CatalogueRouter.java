package com.bheaver.ngl4.esreactor.cataloguing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

//@Configuration
public class CatalogueRouter {
//    @Bean
    public RouterFunction<ServerResponse> route(CataloguingHandler handler){
        return RouterFunctions.route(RequestPredicates.POST("/cataloguing"),handler::importCatalogRecord);
    }
}
