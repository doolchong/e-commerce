package com.example.apigatewayservice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {

    @Value("${JWT_ACCESS_SECRET_KEY}")
    private String accessSecretKey;

    public AuthorizationFilter() {
        super(Config.class);
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header");
            }

            String authorizationHeader = Objects.requireNonNull(
                    request.getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");

            if (!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not valid");
            }

            return chain.filter(exchange);
        });
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        DecodedJWT info;
        try {
            info = JWT
                    .require(Algorithm.HMAC512(accessSecretKey))
                    .build()
                    .verify(jwt); // 검증 성공 시 DecodedJWT 반환
        } catch (TokenExpiredException e) {
            throw new TokenExpiredException("Expired JWT token", e.getExpiredOn());
        } catch (SignatureVerificationException e) {
            throw new JWTVerificationException("Invalid JWT signature");
        } catch (AlgorithmMismatchException e) {
            throw new JWTVerificationException("Unsupported JWT token");
        } catch (JWTDecodeException | IllegalArgumentException e) {
            throw new JWTVerificationException("Invalid JWT claims");
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("JWT verification failed");
        }

        String subject = info.getSubject();

        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }

        return returnValue;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        log.error(errorMessage);

        return response.setComplete();
    }
}
