package com.heachi.auth.api.service.oauth;

import com.heachi.admin.common.exception.oauth.OAuthException;
import com.heachi.auth.TestConfig;
import com.heachi.auth.api.service.oauth.adapter.kakao.OAuthKakaoAdapter;
import com.heachi.auth.api.service.oauth.adapter.naver.OAuthNaverAdapter;
import com.heachi.auth.api.service.oauth.builder.KakaoURLBuilder;
import com.heachi.auth.api.service.oauth.builder.NaverURLBuilder;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.heachi.mysql.define.user.constant.UserPlatformType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class OAuthServiceTest extends TestConfig {

    @MockBean
    private OAuthKakaoAdapter oAuthKakaoAdapter;

    @MockBean
    private OAuthNaverAdapter oAuthNaverAdapter;

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

    @Test
    @DisplayName("Kakao Login에 성공하면, OAuthResponse 객체를 반환한다.")
    void kakaoLoginSuccessThenReturnOAuthResponse() {
        // given
        UserPlatformType platformType = KAKAO;
        String code = "SuccessCode";
        String state = "SuccessState";
        OAuthResponse oAuthResponse = OAuthResponse.builder()
                .platformId("12345")
                .platformType(platformType)
                .email("kms@kakao.com")
                .name("김민수")
                .profileImageUrl("google.com")
                .build();


        when(oAuthKakaoAdapter.getToken(any(String.class))).thenReturn("goodToken");
        when(oAuthKakaoAdapter.getProfile(any(String.class))).thenReturn(oAuthResponse);

        // when
        OAuthResponse profile = oAuthService.login(platformType, code, state);

        // then
        assertThat(profile)
                .extracting("platformId", "platformType", "email", "name", "profileImageUrl")
                .contains("12345", UserPlatformType.KAKAO, "kms@kakao.com", "김민수", "google.com");
    }

    @Test
    @DisplayName("Kakao Login에 실패하면, OAuthException을 반환한다.")
    void kakaoLoginFailThrowsOAuthException() {
        // given
        UserPlatformType platformType = KAKAO;
        String code = "DeniedCode";
        String state = "DeniedState";


        // when
        when(oAuthKakaoAdapter.getToken(any(String.class))).thenThrow(OAuthException.class);
        assertThatThrownBy(() -> oAuthService.login(platformType, code, state))
                // then
                .isInstanceOf(OAuthException.class);
    }

    @Test
    @DisplayName("네이버 로그인에 성공하면 OAuthResponse 객체를 반환한다.")
    void naverLoginSuccessThenReturnOAuthResponse() {
        //given
        UserPlatformType platformType = NAVER;
        String code = "SuccessCode";
        String state = "state";

        OAuthResponse oAuthResponse = OAuthResponse.builder()
                .platformId("123")
                .platformType(platformType)
                .email("hgd@naver.com")
                .name("홍길동")
                .profileImageUrl("naver.com")
                .build();

        when(oAuthNaverAdapter.getToken(any(String.class)))
                .thenReturn("goodToken");
        when(oAuthNaverAdapter.getProfile(any(String.class)))
                .thenReturn(oAuthResponse);

        // when
        OAuthResponse profile = oAuthService.login(platformType, code, state);

        // then
        assertThat(profile)
                .extracting("platformId", "platformType", "email", "name", "profileImageUrl")
                .contains("123", NAVER, "hgd@naver.com", "홍길동", "naver.com");
    }

    @Test
    @DisplayName("네이버 로그인에 실패하면 OAuthException을 반환한다.")
    void naverLoginFailThrowsOAuthException() {
        // given
        UserPlatformType platformType = NAVER;
        String code = "DeniedCode";
        String state = "state";

        when(oAuthNaverAdapter.getToken(any(String.class)))
                .thenThrow(OAuthException.class);

        // when
        assertThatThrownBy(() -> oAuthService.login(platformType, code, state))
                .isInstanceOf(OAuthException.class);
    }
}