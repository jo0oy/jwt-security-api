package com.jo0oy.jwtsecurityapi.interfaces.auth;

import com.jo0oy.jwtsecurityapi.domain.auth.AuthCommand;
import com.jo0oy.jwtsecurityapi.domain.auth.AuthInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AuthDtoMapper {

    // DTO -> COMMAND
    AuthCommand.LoginRequest of(AuthDto.LoginRequest request);

    // INFO -> DTO
    AuthDto.TokenInfo of(AuthInfo.TokenInfo info);

    AuthDto.AccessToken of(AuthInfo.AccessToken info);
}
