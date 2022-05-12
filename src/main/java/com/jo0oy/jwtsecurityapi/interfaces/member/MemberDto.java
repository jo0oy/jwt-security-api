package com.jo0oy.jwtsecurityapi.interfaces.member;

import com.jo0oy.jwtsecurityapi.domain.member.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

public class MemberDto {

    @Getter
    @Builder
    @ToString
    public static class SaveMemberRequest {
        private String username;
        private String password;
        private String email;
        private String phoneNumber;
        private Role role;
    }

    @Getter
    @Builder
    @ToString
    public static class SaveMemberResponse {
        private Long savedId;
    }


    @Getter
    @Builder
    @ToString
    public static class MemberInfoResponse {
        private String username;
        private String email;
        private String phoneNumber;
        private Role role;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
    }
}
