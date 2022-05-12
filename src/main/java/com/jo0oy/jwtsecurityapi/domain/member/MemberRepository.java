package com.jo0oy.jwtsecurityapi.domain.member;

import com.jo0oy.jwtsecurityapi.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    Optional<Member> findByUsernameAndEmail(String username, String email);

    Optional<Member> findByUsernameAndPhoneNumber(String username, String phoneNumber);
}
