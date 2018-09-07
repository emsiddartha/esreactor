package com.bheaver.ngl4.esreactor.aa;

import com.bheaver.ngl4.aa.Authentication;
import com.bheaver.ngl4.aa.exception.AuthenticationException;
import com.bheaver.ngl4.aa.request.LoginRequest;
import com.bheaver.ngl4.aa.response.LoginResponse;
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
        //Logger logger = NGLLogger.getInstance(AAHandler.class,request);
        //final Logger logger = Logger.getLogger(AAHandler.class.getName());
        /*return authentication.login(request.bodyToMono(NGLRequest.class)).handle((loginResponse, throwable) -> {
            //logger.debug("loginResponse..{}",loginResponse);
            if(throwable==null){
                return;ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(loginResponse, LoginResponse.class);
                //return new ResponseEntity(loginResponse, HttpStatus.OK);
            }else {
                if(throwable.getCause() instanceof AuthenticationException){
                    return new ResponseEntity(NGLException.transformNGLExceptionToResponseBody.apply((AuthenticationException)throwable.getCause()),HttpStatus.UNAUTHORIZED);
                }else{
                    //logger.error("Exception...",throwable);
                    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        });

        System.out.println(request.attribute("TenancyId"));
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromObject("Hello, Spring!"));
    }*/

        Mono<LoginResponse> loginResponseMono = authentication.login(request.body(BodyExtractors.toMono(NGLRequest.class)));
        Mono<ServerResponse> monres = loginResponseMono.flatMap(loginResponse -> {
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromObject(loginResponse));
            }
        ).onErrorMap(throwable -> throwable);
        return monres;
    }
}
