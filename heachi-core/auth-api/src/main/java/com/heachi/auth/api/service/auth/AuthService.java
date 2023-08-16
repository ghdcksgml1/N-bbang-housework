package com.heachi.auth.api.service.auth;

import com.heachi.auth.api.service.auth.request.AuthServiceRegisterRequest;
import com.heachi.auth.api.service.auth.response.AuthServiceLoginResponse;
import com.heachi.auth.api.service.oauth.OAuthService;
import com.heachi.auth.api.service.oauth.request.OAuthRegisterRequest;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import com.heachi.mysql.define.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OAuthService oAuthService;

    public AuthServiceLoginResponse login(UserPlatformType platformType, String code, String state) {
        OAuthResponse loginResponse = oAuthService.login(platformType, code, state);
        log.info(">>>> {}님이 로그인하셨습니다.", loginResponse.getName());

        return null;
    }

    public AuthServiceLoginResponse register(UserPlatformType platformType, AuthServiceRegisterRequest request) {

        return null;
    }
}
