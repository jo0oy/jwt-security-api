package com.jo0oy.jwtsecurityapi.service;

import com.jo0oy.jwtsecurityapi.common.exception.JwtExpiredException;
import com.jo0oy.jwtsecurityapi.domain.auth.AuthCommand;
import com.jo0oy.jwtsecurityapi.domain.auth.AuthInfo;
import com.jo0oy.jwtsecurityapi.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    public AuthInfo.TokenInfo login(AuthCommand.LoginRequest command) {

        var authenticationToken
                = new UsernamePasswordAuthenticationToken(command.getUsername(), command.getPassword());

        var authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        var tokenInfo = jwtTokenProvider.generateToken(authentication);

        // redis 에 refreshToken 저장
        redisTemplate.opsForValue().set(authentication.getName(),
                tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return tokenInfo;
    }

    @Override
    @Transactional
    public AuthInfo.AccessToken reissue(String accessToken) {

        log.info("AuthService 토큰 재발행 로직 실행");
        var authentication = jwtTokenProvider.getAuthentication(accessToken);
        var redisKey = authentication.getName();

        log.info("redis key={}", redisKey);

        var refreshToken = (String) redisTemplate.opsForValue().get(redisKey);

        if (Objects.isNull(refreshToken)) {
            throw new JwtExpiredException("리프레시 토큰이 만료되었습니다. 재로그인 해주세요.");
        }

        // 리프레시 토큰이 유효한 경우
        // 이미 있는 리프레시 토큰 삭제
        redisTemplate.delete(redisKey);

        var tokenInfo = jwtTokenProvider.generateToken(authentication);

        redisTemplate.opsForValue().set(redisKey, tokenInfo.getRefreshToken(),
                tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return AuthInfo.AccessToken.builder()
                .accessToken(tokenInfo.getAccessToken())
                .build();
    }
}
