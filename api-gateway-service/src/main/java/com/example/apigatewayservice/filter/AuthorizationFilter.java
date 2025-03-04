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
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RefreshScope
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {

    @Value("${jwt.token.access.secret.key}")
    private String accessSecretKey;

    public AuthorizationFilter() {
        super(Config.class);
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info(accessSecretKey);
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header");
            }

            String authorizationHeader = Objects.requireNonNull(
                    request.getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);

            try {
                // JWT 토큰 검증 및 사용자 정보 추출
                DecodedJWT decodedJWT = validateToken(authorizationHeader);

                // 사용자 정보를 헤더에 추가
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header("X-User-Id", decodedJWT.getSubject())
                        .header("X-User-Role", decodedJWT.getClaim("role").asString())
                        // 필요한 경우 추가 정보도 헤더에 포함
                        .build();

                // 변경된 요청으로 교환 객체 업데이트
                ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(mutatedRequest)
                        .build();

                // 변경된 교환 객체로 필터 체인 계속 진행
                return chain.filter(mutatedExchange);

            } catch (Exception e) {
                log.error("JWT validation error: {}", e.getMessage());
                return onError(exchange, e.getMessage());
            }
        });
    }

    // 토큰 검증 및 디코딩
    public DecodedJWT validateToken(String token) {
        token = token.replace("Bearer ", ""); // Bearer 접두사 제거

        try {
            return JWT
                    .require(Algorithm.HMAC512(accessSecretKey))
                    .build()
                    .verify(token); // 검증 성공 시 DecodedJWT 반환
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
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        log.error(errorMessage);

        return response.setComplete();
    }
}
