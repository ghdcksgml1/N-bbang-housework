package com.heachi.auth.api.service.oauth.adapter.naver;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.oauth.OAuthException;
import com.heachi.auth.api.service.oauth.adapter.OAuthAdapter;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.external.clients.oauth2.naver.NaverProfileClients;
import com.heachi.external.clients.oauth2.naver.NaverTokenClients;
import com.heachi.external.clients.oauth2.naver.response.NaverProfileResponse;
import com.heachi.external.clients.oauth2.naver.response.NaverTokenResponse;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;

import static com.heachi.mysql.define.user.constant.UserPlatformType.NAVER;

@Component
@RequiredArgsConstructor
public class OAuthNaverAdapter implements OAuthAdapter {

    private final NaverTokenClients naverTokenClients;
    private final NaverProfileClients naverProfileClients;

    @Override
    public String getToken(String tokenURL) {
        try {
            NaverTokenResponse token = naverTokenClients.getToken(URI.create(tokenURL));

            if (token.getAccess_token().isEmpty()) {
                throw new OAuthException(ExceptionMessage.OAUTH_INVALID_TOKEN_URL);
            }

            return token.getAccess_token();
        } catch (RuntimeException e) {
            throw new OAuthException(ExceptionMessage.OAUTH_INVALID_TOKEN_URL);
        }
    }

    @Override
    public OAuthResponse getProfile(String accessToken) {
        try {
            NaverProfileResponse profile = naverProfileClients.getProfile("Bearer " + accessToken);

            OAuthResponse oAuthResponse = OAuthResponse.builder()
                    .platformId(profile.getResponse().getId().toString())
                    .platformType(NAVER)
                    .email(profile.getResponse().getEmail())
                    .name(profile.getResponse().getNickname())
                    .profileImageUrl(profile.getResponse().getProfile_image())
                    .build();

            return oAuthResponse;
        } catch (RuntimeException e) {
            throw new OAuthException(ExceptionMessage.OAUTH_INVALID_ACCESS_TOKEN);
        }
    }
}
