package com.jo0oy.jwtsecurityapi.service;

import com.jo0oy.jwtsecurityapi.domain.member.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberInfoMapper memberInfoMapper;

    @Override
    @Transactional
    public Long save(MemberCommand.SaveMemberRequest command) {
        log.info("회원 저장");

        validateDuplicateUsername(command.getUsername());
        var member = command.toEntity();
        member.setEncodedPassword(passwordEncoder.encode(member.getPassword()));

        return memberRepository.save(member).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfo.MainInfo getMember(Long memberId) {
        log.info("회원 조회 by memberId");

        return memberInfoMapper.of(memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 회원입니다 id={}", memberId);
                    throw new IllegalArgumentException("존재하지 않는 회원입니다 id=" + memberId);
                }));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfo.MainInfo getMember(String username) {
        log.info("회원 조회 by username");

        return memberInfoMapper.of(memberRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 회원입니다 username={}", username);
                    throw new IllegalArgumentException("존재하지 않는 회원입니다 username=" + username);
                }));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfo.MainInfo getMemberByUsernameAndEmail(String username, String email) {
        log.info("회원 조회 by username & email");
        return memberInfoMapper.of(memberRepository.findByUsernameAndEmail(username, email)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 회원입니다 username={}, email={}", username, email);
                    throw new IllegalArgumentException("존재하지 않는 회원입니다 username=" + username
                            + "email=" + email);
                }));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfo.MainInfo getMemberByUsernameAndPhoneNumber(String username, String phoneNumber) {
        log.info("회원 조회 by username & phoneNumber");
        return memberInfoMapper.of(memberRepository.findByUsernameAndPhoneNumber(username, phoneNumber)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 회원입니다 username={}, phoneNumber={}", username, phoneNumber);
                    throw new IllegalArgumentException("존재하지 않는 회원입니다 username=" + username
                            + "phoneNumber=" + phoneNumber);
                }));
    }

    @Override
    @Transactional
    public void update(Long memberId, String email, String phoneNumber) {
        log.info("회원 정보 업데이트 시작");
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 회원입니다 id={}", memberId);
                    throw new IllegalArgumentException("존재하지 않는 회원입니다 id=" + memberId);
                });

        log.info("회원 정보 검증 로직 실행");
        String updateEmail = validateEmail(member.getEmail(), email);
        String updatePhoneNumber = validatePhoneNumber(member.getPhoneNumber(), phoneNumber);

        member.updateMemberInfo(updateEmail, updatePhoneNumber);
    }

    private String validatePhoneNumber(String originPhoneNumber, String phoneNumber) {
        log.info("전화번호 검증 로직 실행");
        if (StringUtils.hasText(phoneNumber) && StringUtils.containsWhitespace(phoneNumber)) {
            log.error("정상적인 이메일과 전화번호 형식이 아닙니다. 공백을 포함할 수 없습니다.");
            throw new IllegalArgumentException("정상적인 이메일과 전화번호 형식이 아닙니다. 공백을 포함할 수 없습니다.");
        } else if (StringUtils.hasText(phoneNumber) && !StringUtils.containsWhitespace(phoneNumber)) {
            return phoneNumber;
        }
        return originPhoneNumber;
    }

    private void validateDuplicateUsername(String username) {
        log.info("중복 유저 이름 확인 로직 실행");
        if (memberRepository.findByUsername(username).isPresent()) {
            log.error("이미 존재하는 username 입니다. username={}", username);
            throw new IllegalArgumentException("이미 존재하는 username 입니다. username=" + username);
        }
    }

    private String validateEmail(String originEmail, String email) {
        log.info("email 검증 로직 실행");
        if (StringUtils.hasText(email) && StringUtils.containsWhitespace(email)) {
            log.error("정상적인 이메일과 전화번호 형식이 아닙니다. 공백을 포함할 수 없습니다.");
            throw new IllegalArgumentException("정상적인 이메일과 전화번호 형식이 아닙니다. 공백을 포함할 수 없습니다.");
        } else if (StringUtils.hasText(email) && !StringUtils.containsWhitespace(email)) {
            return email;
        }

        return originEmail;
    }
}
