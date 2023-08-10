package com.heachi.auth.api.service.oauth.adapter.kakao;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.oauth.OAuthException;
import com.heachi.auth.api.service.oauth.adapter.OAuthAdapter;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.external.clients.oauth2.kakao.KakaoProfileClients;
import com.heachi.external.clients.oauth2.kakao.KakaoTokenClients;
import com.heachi.external.clients.oauth2.kakao.response.KakaoProfileResponse;
import com.heachi.external.clients.oauth2.kakao.response.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;

import static com.heachi.mysql.define.user.constant.UserPlatformType.*;

@Component
@RequiredArgsConstructor
public class OAuthKakaoAdapter implements OAuthAdapter {

    private final KakaoTokenClients kakaoTokenClients;
    private final KakaoProfileClients kakaoProfileClients;

    @Override
    public String getToken(String tokenURL) {
        try {
            KakaoTokenResponse token = kakaoTokenClients.getToken(URI.create(tokenURL));

            return token.getAccess_token();
        } catch (RuntimeException e) {
            throw new OAuthException(ExceptionMessage.OAUTH_INVALID_TOKEN_URL);
        }
    }

    @Override
    public OAuthResponse getProfile(String accessToken) {
        try {
            KakaoProfileResponse profile = kakaoProfileClients.getProfile("Bearer " + accessToken);
            OAuthResponse oAuthResponse = OAuthResponse.builder()
                    .platformId(profile.getId().toString())
                    .platformType(KAKAO)
                    .email(profile.getKakao_account().getEmail())
                    .name(profile.getKakao_account().getProfile().getNickname())
                    .profileImageUrl(profile.getKakao_account().getProfile().getThumbnail_image_url())
                    .build();

            return oAuthResponse;
        } catch (RuntimeException e) {
            throw new OAuthException(ExceptionMessage.OAUTH_INVALID_ACCESS_TOKEN);
        }
    }
}
