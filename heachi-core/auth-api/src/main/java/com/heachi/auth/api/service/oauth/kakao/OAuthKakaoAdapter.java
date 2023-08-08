package com.heachi.auth.api.service.oauth.kakao;

import com.heachi.auth.api.service.oauth.OAuthAdapter;
import com.heachi.auth.api.service.oauth.request.OAuthRegisterRequest;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.mysql.define.user.constant.UserPlatformType;

public class OAuthKakaoAdapter implements OAuthAdapter {

    @Override
    public OAuthResponse login(UserPlatformType platformType, String code) {
        return null;
    }

    @Override
    public OAuthResponse register(OAuthRegisterRequest request) {
        return null;
    }

}
