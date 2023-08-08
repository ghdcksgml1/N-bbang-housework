package com.heachi.auth.api.service.oauth;

import com.heachi.auth.api.service.oauth.request.OAuthRegisterRequest;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.mysql.define.user.constant.UserPlatformType;

/**
 * Adapter 패턴을 이용한 OAuth 2.0 로그인
 */
public interface OAuthAdapter {

    OAuthResponse login(UserPlatformType platformType, String code);
    OAuthResponse register(OAuthRegisterRequest request);
}
