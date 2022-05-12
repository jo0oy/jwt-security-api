package com.jo0oy.jwtsecurityapi.interfaces.member;

import com.jo0oy.jwtsecurityapi.common.response.ResultResponse;
import com.jo0oy.jwtsecurityapi.domain.auth.AuthMember;
import com.jo0oy.jwtsecurityapi.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberServiceImpl memberService;
    private final MemberDtoMapper memberDtoMapper;

    @PostMapping("/api/v1/member")
    public ResponseEntity<?> save(@RequestBody MemberDto.SaveMemberRequest request) {
        var data = memberService.save(memberDtoMapper.of(request));

        return ResponseEntity.created(URI.create("/api/v1/member"))
                .body(memberDtoMapper.of(data));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/v1/member")
    public ResponseEntity<?> getMember(@AuthenticationPrincipal AuthMember authMember) {
        var memberInfo = memberService.getMember(authMember.getUsername());

        return ResponseEntity.ok(ResultResponse.res(true, memberDtoMapper.of(memberInfo)));
    }
}
