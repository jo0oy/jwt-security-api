package com.jo0oy.jwtsecurityapi.service;

import com.jo0oy.jwtsecurityapi.domain.member.Member;
import com.jo0oy.jwtsecurityapi.domain.member.MemberCommand;
import com.jo0oy.jwtsecurityapi.domain.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MemberServiceImplTest {

    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 저장 성공 테스트")
    void save_success_test() {
        //given
        MemberCommand.SaveMemberRequest member = MemberCommand.SaveMemberRequest.builder()
                .username("test1")
                .password("test1111")
                .email("test1@gmail.com")
                .phoneNumber("010-1111-1111")
                .build();

        Long savedId = memberService.save(member);

        //when
        Member findMember = memberRepository.findById(savedId).get();

        //then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getUsername()).isEqualTo("test1");
        assertThat(findMember.getEmail()).isEqualTo("test1@gmail.com");
    }

    @Test
    @DisplayName("회원 정보 수정 성공 테스트")
    @Transactional
    void update_success_test() {
        //given
        Member member = Member.builder()
                .username("test1")
                .password("test1111")
                .email("test1@gmail.com")
                .phoneNumber("010-1111-1111")
                .build();

        Long savedId = memberRepository.save(member).getId();

        em.flush();
        em.clear();

        String updateEmail = "test1update@gmail.com";
        String updatePhoneNumber = "010-1111-1110";

        //when
        memberService.update(savedId, updateEmail, updatePhoneNumber);

        Member findMember = memberRepository.findById(savedId).get();

        //then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getEmail()).isEqualTo(updateEmail);
        assertThat(findMember.getPhoneNumber()).isEqualTo(updatePhoneNumber);
    }

    @Test
    @DisplayName("수정 정보 null 혹은 빈 문자열일때 변화 없음 테스트")
    @Transactional
    void not_change_test() {
        //given
        Member member = Member.builder()
                .username("test1")
                .password("test1111")
                .email("test1@gmail.com")
                .phoneNumber("010-1111-1111")
                .build();

        Long savedId = memberRepository.save(member).getId();

        em.flush();
        em.clear();

        String updatePhoneNumber = " ";

        //when
        memberService.update(savedId, null, updatePhoneNumber);

        Member findMember = memberRepository.findById(savedId).get();

        //then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getEmail()).isEqualTo("test1@gmail.com");
        assertThat(findMember.getPhoneNumber()).isEqualTo("010-1111-1111");

    }

    @Test
    @DisplayName("회원 정보 수정 실패 테스트_수정정보_공백존재")
    @Transactional
    void update_fail_test() {
        //given
        Member member = Member.builder()
                .username("test1")
                .password("test1111")
                .email("test1@gmail.com")
                .phoneNumber("010-1111-1111")
                .build();

        Long savedId = memberRepository.save(member).getId();

        em.flush();
        em.clear();

        String updateEmail = "test1 update@gmail.com";
        String updatePhoneNumber = "010-1111-11 10";

        assertThatThrownBy(() -> memberService.update(savedId, updateEmail, updatePhoneNumber))
                .isInstanceOf(IllegalArgumentException.class);
    }

}