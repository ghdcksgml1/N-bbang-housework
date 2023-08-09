package com.heachi.auth.api.service.oauth.adapter.kakao;

import com.heachi.auth.api.service.oauth.adapter.OAuthAdapter;
import com.heachi.auth.api.service.oauth.builder.KakaoURLBuilder;
import com.heachi.auth.api.service.oauth.request.OAuthRegisterRequest;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthKakaoAdapter implements OAuthAdapter {


    @Override
    public String getToken(String tokenURL) {
        return null;
    }

    @Override
    public OAuthResponse getProfile(String accessToken) {
        return null;
    }
}
