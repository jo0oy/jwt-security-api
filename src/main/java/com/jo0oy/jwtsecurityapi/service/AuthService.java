package com.jo0oy.jwtsecurityapi.service;

import com.jo0oy.jwtsecurityapi.domain.auth.AuthCommand;
import com.jo0oy.jwtsecurityapi.domain.auth.AuthInfo;

public interface AuthService {
    AuthInfo.TokenInfo login(AuthCommand.LoginRequest command);

    AuthInfo.AccessToken reissue(String accessToken);
}
