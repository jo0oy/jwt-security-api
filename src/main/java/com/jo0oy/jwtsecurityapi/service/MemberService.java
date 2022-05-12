package com.jo0oy.jwtsecurityapi.service;

import com.jo0oy.jwtsecurityapi.domain.member.MemberCommand;
import com.jo0oy.jwtsecurityapi.domain.member.MemberInfo;

public interface MemberService {

    Long save(MemberCommand.SaveMemberRequest command);

    MemberInfo.MainInfo getMember(Long memberId);

    MemberInfo.MainInfo getMember(String username);

    MemberInfo.MainInfo getMemberByUsernameAndEmail(String username, String email);

    MemberInfo.MainInfo getMemberByUsernameAndPhoneNumber(String username, String phoneNumber);

    void update(Long memberId, String email, String phoneNumber);

}
