package com.jo0oy.jwtsecurityapi.service;

import com.jo0oy.jwtsecurityapi.domain.auth.AuthMember;
import com.jo0oy.jwtsecurityapi.domain.member.Member;
import com.jo0oy.jwtsecurityapi.domain.member.MemberRepository;
import com.jo0oy.jwtsecurityapi.domain.member.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> {
                    log.error("존재하지 않는 회원입니다. username={}", username);
                    throw new UsernameNotFoundException("존재하지 않는 회원입니다. username=" + username);
                }
        );

        log.info("회원 로딩 by username {}", username);

        List<Role> roles = new ArrayList<>();
        roles.add(member.getRole());

        return new AuthMember(member.getUsername(), member.getPassword(), roles);
    }
}
