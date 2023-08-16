package com.heachi.auth.api.service.oauth;

import com.heachi.auth.api.service.oauth.adapter.OAuthAdapter;
import com.heachi.auth.api.service.oauth.adapter.kakao.OAuthKakaoAdapter;
import com.heachi.auth.api.service.oauth.adapter.naver.OAuthNaverAdapter;
import com.heachi.auth.api.service.oauth.builder.KakaoURLBuilder;
import com.heachi.auth.api.service.oauth.builder.NaverURLBuilder;
import com.heachi.auth.api.service.oauth.builder.OAuthURLBuilder;
import com.heachi.auth.api.service.oauth.request.OAuthRegisterRequest;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.heachi.mysql.define.user.constant.UserPlatformType.*;

@Slf4j
@Service
public class OAuthService {

    private Map<UserPlatformType, OAuthFactory> adapterMap;

    public OAuthService(OAuthKakaoAdapter oAuthKakaoAdapter, OAuthNaverAdapter oAuthNaverAdapter
            , KakaoURLBuilder kakaoURLBuilder, NaverURLBuilder naverURLBuilder) {
        this.adapterMap = new HashMap<>() {{
            put(KAKAO, OAuthFactory.builder()
                    .oAuthAdapter(oAuthKakaoAdapter)
                    .oAuthURLBuilder(kakaoURLBuilder)
                    .build());
            put(NAVER, OAuthFactory.builder()
                    .oAuthAdapter(oAuthNaverAdapter)
                    .oAuthURLBuilder(naverURLBuilder)
                    .build());
        }};
    }

    // OAuth 2.0 로그인 URL 생성기
    public String loginPage(UserPlatformType platformType, String state) {

        return adapterMap.get(platformType)
                .getOAuthURLBuilder()
                .authorize(state);
    }

    // OAuth 2.0 로그인
    public OAuthResponse login(UserPlatformType platformType, String code, String state) {
        OAuthFactory factory = getOAuthFactory(platformType);
        OAuthURLBuilder urlBuilder = factory.getOAuthURLBuilder();
        OAuthAdapter adapter = factory.getOAuthAdapter();
        log.info(">>>> {} Login Start", platformType);

        String tokenURL = urlBuilder.token(code, state);
        String accessToken = adapter.getToken(tokenURL);
        OAuthResponse userInfo = adapter.getProfile(accessToken);
        log.info(">>>> {} Login Success", platformType);

        return userInfo;
    }

    private OAuthFactory getOAuthFactory(UserPlatformType platformType) {
        return adapterMap.get(platformType);
    }
}
