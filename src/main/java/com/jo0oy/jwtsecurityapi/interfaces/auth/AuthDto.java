package com.jo0oy.jwtsecurityapi.interfaces.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class AuthDto {

    @Getter
    @Builder
    @ToString
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class ReissueRequest {
        private String accessToken;

        public ReissueRequest(String accessToken) {
            this.accessToken = accessToken;
        }
    }



    @Getter
    @Builder
    @ToString
    public static class TokenInfo {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Builder
    @ToString
    public static class AccessToken {
        private String accessToken;
    }

}
