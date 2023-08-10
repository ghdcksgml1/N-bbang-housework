package com.heachi.auth.api.service.oauth.adapter.naver;

import com.heachi.auth.api.service.oauth.adapter.OAuthAdapter;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import org.springframework.stereotype.Component;

@Component
public class OAuthNaverAdapter implements OAuthAdapter {

    @Override
    public String getToken(String tokenURL) {
        return null;
    }

    @Override
    public OAuthResponse getProfile(String accessToken) {
        return null;
    }
}
