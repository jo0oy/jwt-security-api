package com.jo0oy.jwtsecurityapi.security;

import com.jo0oy.jwtsecurityapi.common.exception.JwtExpiredException;
import com.jo0oy.jwtsecurityapi.domain.auth.AuthInfo;
import com.jo0oy.jwtsecurityapi.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider implements InitializingBean {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private static final String AUTHORITIES_KEY = "auth";

    private final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 1; // 3분

    private final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 3; // 5분

    private Key key;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // jwt 토큰 생성
    public AuthInfo.TokenInfo generateToken(Authentication authentication) {

        var authorities = getAuthorities(authentication);
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenValidity = new Date(now + this.REFRESH_TOKEN_EXPIRE_TIME);

        var accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();

        var refreshToken = Jwts.builder()
                        .setExpiration(refreshTokenValidity)
                        .signWith(key, SignatureAlgorithm.HS512).compact();

        return AuthInfo.TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        UserDetails principal = customUserDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }


    // jwt 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (MalformedJwtException e) {
            log.info("잘못된 JWT 토큰입니다.");
        } catch (ExpiredJwtException e) {
                log.info("만료된 JWT 토큰입니다.");
            throw new JwtExpiredException("만료된 토큰");
        } catch (UnsupportedJwtException e) {
                log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
                log.info("JWT 토큰이 잘못되었습니다.");
        }
            return false;
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims();
        }
    }

    // 권한 리스트 추출
    private List<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }
}
