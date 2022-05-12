package com.jo0oy.jwtsecurityapi.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

public class MemberInfo {

    @Getter
    @Builder
    @ToString
    public static class MainInfo {
        private Long memberId;
        private String username;
        private String email;
        private String phoneNumber;
        private Role role;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
    }
}
