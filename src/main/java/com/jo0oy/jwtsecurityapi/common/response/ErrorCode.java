package com.jo0oy.jwtsecurityapi.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    COMMON_ENTITY_NOT_FOUND(BAD_REQUEST.value(), "존재하지 않는 엔티티입니다."),
    SECURITY_UNAUTHORIZED(UNAUTHORIZED.value(), "인증에 실패했습니다.");

    private final Integer status;
    private final String errorMsg;
}
