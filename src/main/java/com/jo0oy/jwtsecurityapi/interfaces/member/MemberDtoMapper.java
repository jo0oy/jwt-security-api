package com.jo0oy.jwtsecurityapi.interfaces.member;

import com.jo0oy.jwtsecurityapi.domain.member.MemberCommand;
import com.jo0oy.jwtsecurityapi.domain.member.MemberInfo;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MemberDtoMapper {

    // DTO -> COMMAND
    MemberCommand.SaveMemberRequest of(MemberDto.SaveMemberRequest request);

    // INFO -> DTO
    @Mappings({
            @Mapping(source = "info.createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(source = "info.lastModifiedAt", target = "lastModifiedAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    MemberDto.MemberInfoResponse of(MemberInfo.MainInfo info);

    MemberDto.SaveMemberResponse of(Long savedId);
}
