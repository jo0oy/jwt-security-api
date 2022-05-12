package com.jo0oy.jwtsecurityapi.domain.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class AuthCommand {

    @Getter
    @Builder
    @ToString
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
