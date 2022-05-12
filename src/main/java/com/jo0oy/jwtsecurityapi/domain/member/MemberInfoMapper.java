package com.jo0oy.jwtsecurityapi.domain.member;

import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MemberInfoMapper {

    // ENTITY -> INFO
    @Mappings(@Mapping(source = "member.id", target = "memberId"))
    MemberInfo.MainInfo of(Member member);

}
