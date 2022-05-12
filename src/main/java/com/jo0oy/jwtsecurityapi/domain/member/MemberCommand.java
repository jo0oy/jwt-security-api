package com.jo0oy.jwtsecurityapi.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class MemberCommand {

    @Builder
    @Getter
    @ToString
    public static class SaveMemberRequest {
        private String username;
        private String password;
        private String email;
        private String phoneNumber;
        private Role role;

        public Member toEntity() {
            return Member.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .role(role)
                    .build();
        }
    }

}
