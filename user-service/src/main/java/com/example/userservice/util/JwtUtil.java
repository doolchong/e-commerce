package com.example.userservice.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RefreshScope
public class JwtUtil {

    private final String REFRESH_TOKEN_HEADER = "refreshToken";

    private final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.token.access.secret.key}")
    private String accessSecretKey;

    @Value("${jwt.token.refresh.secret.key}")
    private String refreshSecretKey;

    public String createAccessToken(String userId) {
        return BEARER_PREFIX + JWT.create()
                .withSubject(userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 30 * 1000L)) // 30분
                .sign(Algorithm.HMAC512(accessSecretKey));
    }

    public String createRefreshToken(String userId, String role) {
        return BEARER_PREFIX + JWT.create()
                .withSubject(userId)
                .withClaim("role", role)
                .withExpiresAt(
                        new Date(System.currentTimeMillis() + 60 * 60 * 24 * 14 * 1000L) // 2주
                )
                .sign(Algorithm.HMAC512(refreshSecretKey));
    }

    // Access Token 헤더 설정
    public void addAccessTokenToHeader(HttpServletResponse response, String accessToken) {
        response.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
    }

    // Refresh Token 쿠키 설정
    public void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        refreshToken = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        Cookie cookie = new Cookie(REFRESH_TOKEN_HEADER, refreshToken);
        cookie.setHttpOnly(true); // XSS 공격 방지
        cookie.setMaxAge(60 * 60 * 24 * 14);
        cookie.setPath("/"); // 쿠키의 경로를 루트로 설정
        cookie.setSecure(false); // 운영환경에서는 true(https)
//        cookie.setDomain("example.com");
        cookie.setAttribute("SameSite", "Lax"); // 프론트와 백엔드가 도메인이 다르면 Lax 또는 None
        response.addCookie(cookie);
    }

    // header 에서 JWT 가져오기
    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 쿠키에서 Refresh Token 추출
    public String resolveTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(REFRESH_TOKEN_HEADER)) {
                    // 쿠키 값 디코딩
                    return java.net.URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                }
            }
        }
        return null;  // 쿠키가 없으면 null 반환
    }
}