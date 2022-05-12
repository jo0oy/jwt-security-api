package com.jo0oy.jwtsecurityapi.interfaces.auth;

import com.jo0oy.jwtsecurityapi.common.response.ResultResponse;
import com.jo0oy.jwtsecurityapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthApiController {

    private final AuthService authService;
    private final AuthDtoMapper authDtoMapper;

    @PostMapping("/api/v1/login")
    public ResponseEntity<?> login(@RequestBody AuthDto.LoginRequest request) {
        log.info("AuthApiController post login 호출");
        var tokenInfo = authService.login(authDtoMapper.of(request));

        return ResponseEntity.ok(ResultResponse.res(true, authDtoMapper.of(tokenInfo)));
    }

    @PostMapping("/api/v1/reissue")
    public ResponseEntity<?> reissue(@RequestBody AuthDto.ReissueRequest request) {
        log.info("AuthApiController post reissue 호출");

        var tokenInfo = authService.reissue(request.getAccessToken());

        return ResponseEntity.ok(ResultResponse.res(true, authDtoMapper.of(tokenInfo)));
    }
}
