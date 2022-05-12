package com.jo0oy.jwtsecurityapi.domain;

import com.jo0oy.jwtsecurityapi.domain.member.Member;
import com.jo0oy.jwtsecurityapi.domain.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("1. member save 테스트")
    void memberSaveTest() {
        //given
        Member member = Member.builder()
                .username("test1")
                .password("test1test1")
                .email("test1@gmail.com")
                .phoneNumber("010-1111-1111")
                .build();

        Member savedMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        //then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getUsername()).isEqualTo("test1");
        assertThat(findMember.getPhoneNumber()).isEqualTo("010-1111-1111");
    }

    @Test
    @DisplayName("2. find member by username 테스트")
    void findMemberByUsernameTest() {
        //given
        Member member = Member.builder()
                .username("test1")
                .password("test1test1")
                .email("test1@gmail.com")
                .phoneNumber("010-1111-1111")
                .build();

        Member savedMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findByUsername(savedMember.getUsername()).get();

        //then
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
    }

}