package com.jo0oy.jwtsecurityapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jo0oy.jwtsecurityapi.common.response.ErrorCode;
import com.jo0oy.jwtsecurityapi.common.response.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.info("CustomAuthenticationEntryPoint errorResponse 생성 로직 실행");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        var errorResponse =
                ResultResponse.error(ResultResponse.Error.builder()
                        .errorCode(ErrorCode.SECURITY_UNAUTHORIZED.getStatus())
                        .errorMessage(authException.getMessage())
                        .errorDetails(null)
                        .build());

        try (var os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, errorResponse);
            os.flush();
        }
    }
}
