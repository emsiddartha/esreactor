package com.bheaver.ngl4.esreactor.filters;

import com.bheaver.ngl4.aa.JWT;
import com.bheaver.ngl4.aa.factory.AAFactory;
import com.bheaver.ngl4.importCatalog.factory.ImportCatalogFactory;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.KeyStoreException;

@Component
@Import({AAFactory.class, ImportCatalogFactory.class})
public class AuthenticationFilter implements WebFilter {
    @Autowired
    @Qualifier("JWT")
    private JWT jwt;
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain){
        String xAuthToken = serverWebExchange.getRequest().getHeaders().getFirst("x-auth-token");

        boolean validateAuthToken = false;
        if(validateAuthToken && shouldBeLoggedIn(serverWebExchange)){
        try {
            validateAuthToken = jwt.validateJWTToken(xAuthToken);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (InvalidJwtException e) {
            e.printStackTrace();
        }

            try {
                serverWebExchange.getResponse()
                        .getHeaders().add("x-auth-token",jwt.renewJWTToken(xAuthToken));
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (InvalidJwtException e) {
                e.printStackTrace();
            } catch (MalformedClaimException e) {
                e.printStackTrace();
            }
        }
        return webFilterChain.filter(serverWebExchange);
    }

    private Boolean shouldBeLoggedIn(ServerWebExchange serverWebExchange){
        return serverWebExchange.getRequest().getURI().getPath().equals("login");
    }
}
