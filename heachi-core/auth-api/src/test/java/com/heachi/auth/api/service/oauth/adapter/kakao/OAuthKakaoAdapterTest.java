package com.heachi.auth.api.service.oauth.adapter.kakao;

import com.heachi.admin.common.exception.oauth.OAuthException;
import com.heachi.auth.TestConfig;
import com.heachi.auth.api.service.oauth.builder.KakaoURLBuilder;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.external.clients.oauth2.kakao.KakaoProfileClients;
import com.heachi.external.clients.oauth2.kakao.KakaoTokenClients;
import com.heachi.external.clients.oauth2.kakao.response.KakaoProfileResponse;
import com.heachi.external.clients.oauth2.kakao.response.KakaoTokenResponse;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class OAuthKakaoAdapterTest extends TestConfig {

    @Autowired
    private OAuthKakaoAdapter oAuthKakaoAdapter;

    @Autowired
    private KakaoURLBuilder kakaoURLBuilder;

    @Test
    @DisplayName("카카오 토큰 API에 정상적인 요청을 보내면, access_token이 발행된다.")
    void kakaoAdapterGetToken() {
        // given
        KakaoTokenClients kakaoTokenClients = new MockKakaoTokenClients();
        KakaoProfileClients kakaoProfileClients = new MockKakaoProfileClients();
        OAuthKakaoAdapter oAuthKakaoAdapter = new OAuthKakaoAdapter(kakaoTokenClients, kakaoProfileClients);

        // when
        String accessToken = oAuthKakaoAdapter.getToken("myUrl");

        // then
        System.out.println("accessToken = " + accessToken);
        assertThat(accessToken).isEqualTo("accessToken");
    }

    @Test
    @DisplayName("카카오 토큰 API에 요청 중 예외가 발생하면, Adapter에서 OAuthException으로 예외 처리를 한다.")
    void kakaoAdapterGetTokenWhenApiThrowsException() {
        // given
        String tokenUrl = kakaoURLBuilder.token("DeniedToken", "state");

        // when
        assertThatThrownBy(() -> oAuthKakaoAdapter.getToken(tokenUrl))
                // then
                .isInstanceOf(OAuthException.class);
    }

    @Test
    @DisplayName("카카오 프로필 API에 정상적인 요청을 보내면, 사용자 정보를 가져온다.")
    void kakaoAdapterGetProfile() {
        // given
        KakaoTokenClients kakaoTokenClients = new MockKakaoTokenClients();
        KakaoProfileClients kakaoProfileClients = new MockKakaoProfileClients();
        OAuthKakaoAdapter oAuthKakaoAdapter = new OAuthKakaoAdapter(kakaoTokenClients, kakaoProfileClients);

        // when
        OAuthResponse profile = oAuthKakaoAdapter.getProfile("accessToken");

        // then
        assertThat(profile)
                .extracting("platformId", "platformType", "email", "name", "profileImageUrl")
                .contains("12345", UserPlatformType.KAKAO, "kms@kakao.com", "김민수", "google.com");
    }

    @Test
    @DisplayName("카카오 프로필 API에 요청 중 예외가 발생하면, Adapter에서 OAuthException으로 예외 처리를 한다.")
    void kakaoAdapterGetProfileWhenApiThrowsException() {
        // given
        String accessToken = "DeniedToken";

        // when
        assertThatThrownBy(() -> oAuthKakaoAdapter.getProfile(accessToken))
                // then
                .isInstanceOf(OAuthException.class);
    }

    static class MockKakaoTokenClients implements KakaoTokenClients {

        @Override
        public KakaoTokenResponse getToken(URI uri) {

            return new KakaoTokenResponse("accessToken");
        }
    }

    static class MockKakaoProfileClients implements KakaoProfileClients {

        @Override
        public KakaoProfileResponse getProfile(String headers) {
            return new KakaoProfileResponse(12345L,
                    new KakaoProfileResponse.KakaoAccount("kms@kakao.com",
                            new KakaoProfileResponse.KakaoAccount.KakaoProfile("김민수", "google.com")));
        }
    }
}