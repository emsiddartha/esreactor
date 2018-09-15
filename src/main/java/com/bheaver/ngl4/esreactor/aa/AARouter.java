package com.bheaver.ngl4.esreactor.aa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class AARouter {

    //@Bean
    public RouterFunction<ServerResponse> route(AAHandler handler){
        return RouterFunctions.route(RequestPredicates.POST("/login"),handler::authenticate)
                .andRoute(RequestPredicates.OPTIONS("/**"),handler::corsCall);
    }
}
