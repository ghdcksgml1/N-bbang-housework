package com.heachi.auth.api.service.oauth;

import com.heachi.auth.TestConfig;
import com.heachi.auth.api.service.oauth.builder.KakaoURLBuilder;
import com.heachi.auth.api.service.oauth.builder.NaverURLBuilder;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.heachi.mysql.define.user.constant.UserPlatformType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OAuthServiceTest extends TestConfig {

    @Autowired
    private OAuthService oAuthService;

    @Autowired
    private KakaoURLBuilder kakaoURLBuilder;

    @Autowired
    private NaverURLBuilder naverURLBuilder;

    @Test
    @DisplayName("platformType에 맞는 알맞은 url을 리턴한다.")
    void urlGeneratorIsGood() {
        // given
        String state = "CsrfDetecting";
        String kakaoLoginPage = kakaoURLBuilder.authorize(state);
        String naverLoginPage = naverURLBuilder.authorize(state);

        // when
        String kakaoResult = oAuthService.loginPage(KAKAO, state);
        String naverResult = oAuthService.loginPage(NAVER, state);

        // then
        assertThat(kakaoResult).isEqualTo(kakaoLoginPage);
        assertThat(naverResult).isEqualTo(naverLoginPage);
    }
}