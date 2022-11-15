package com.example.gatewayservice.config;


import com.example.gatewayservice.config.JwtUtil;
import com.example.gatewayservice.config.RouterValidator;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter{

    @Autowired
    private RouterValidator routerValidator;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        System.out.println("testtttttttttttttttt");
        ServerHttpRequest request = exchange.getRequest();

        if(routerValidator.isSecured.test(request)){
            System.out.println("test2222222");

            if(this.isAuthMissing(request)){
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
            }


            System.out.println("test3333");
            final String token = this.getAuthHeader(request);
            System.out.println("test44444");

//            if(jwtUtil.isInvalid(token)){
//                System.out.println("test5555555");
//                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
//            }
//            System.out.println("test66666");


            this.populateRequestWithHeaders(exchange, token);


        }
        return chain.filter(exchange);
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        System.out.println("testtttttttttttttttt");
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        exchange.getRequest().mutate()
                .header("id", String.valueOf(claims.get("id")))
                .header("role", String.valueOf(claims.get("role")))
                .build();

    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        System.out.println("isAuthM");
        return !request.getHeaders().containsKey("Authorization");

    }

    private String getAuthHeader(ServerHttpRequest request) {
        System.out.println("getAuthH");
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }


    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }



}
