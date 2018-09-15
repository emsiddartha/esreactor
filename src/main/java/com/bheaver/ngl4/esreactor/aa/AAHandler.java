package com.bheaver.ngl4.esreactor.aa;

import com.bheaver.ngl4.aa.Authentication;
import com.bheaver.ngl4.aa.exception.AuthenticationException;
import com.bheaver.ngl4.aa.request.LoginRequest;
import com.bheaver.ngl4.aa.response.LoginResponse;
import com.bheaver.ngl4.exception.ExceptionJSONSerializer;
import com.bheaver.ngl4.exception.NGLException;
import com.bheaver.ngl4.httprequest.NGLRequest;
import com.bheaver.ngl4.httprequest.NGLRequestHeaderWithTenancy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import java.util.logging.Logger;

@Component
public class AAHandler {
    @Autowired
    @Qualifier("Authentication")
    Authentication authentication;

    public Mono<ServerResponse> authenticate(ServerRequest request){
        Mono<LoginResponse> loginResponseMono = authentication.login(request.body(BodyExtractors.toMono(NGLRequest.class)));
        Mono<ServerResponse> monres = loginResponseMono.flatMap(loginResponse -> {
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromObject(loginResponse));
            }
        ).onErrorResume(throwable -> {
            if(throwable instanceof AuthenticationException){
                return ServerResponse.status(401).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(((AuthenticationException)throwable).getJSONString()));
            }else{
                return ServerResponse.status(500).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject("Internal Server Error"));
            }
        });
        return monres;
    }
    public Mono<ServerResponse> corsCall(ServerRequest request){
        return ServerResponse.status(200).headers(httpHeaders -> {
            httpHeaders.add("Access-Control-Allow-Origin", "*");
            httpHeaders.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            httpHeaders.add("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with");
            httpHeaders.add("Access-Control-Max-Age", "3600");
        }).body(BodyInserters.empty());
    }
}
