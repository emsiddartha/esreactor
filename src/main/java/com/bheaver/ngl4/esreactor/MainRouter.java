package com.bheaver.ngl4.esreactor;

import com.bheaver.ngl4.esreactor.aa.AAHandler;
import com.bheaver.ngl4.esreactor.aa.AARouter;
import com.bheaver.ngl4.esreactor.cataloguing.CatalogueRouter;
import com.bheaver.ngl4.esreactor.cataloguing.CataloguingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class MainRouter {

    @Autowired
    AAHandler aaHandler;

    @Autowired
    CataloguingHandler cataloguingHandler;

    @Bean
    public RouterFunction<ServerResponse> route(){
        return RouterFunctions.route(RequestPredicates.POST("/cataloguing"),cataloguingHandler::importCatalogRecord)
                .andRoute(RequestPredicates.POST("/login"),aaHandler::authenticate);
    }
}
