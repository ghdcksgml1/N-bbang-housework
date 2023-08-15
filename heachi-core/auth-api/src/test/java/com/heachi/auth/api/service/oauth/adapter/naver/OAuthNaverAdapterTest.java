package com.heachi.auth.api.service.oauth.adapter.naver;

import com.heachi.admin.common.exception.oauth.OAuthException;
import com.heachi.auth.TestConfig;
import com.heachi.auth.api.service.oauth.builder.NaverURLBuilder;
import com.heachi.auth.api.service.oauth.builder.OAuthURLBuilder;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.external.clients.oauth2.naver.NaverProfileClients;
import com.heachi.external.clients.oauth2.naver.NaverTokenClients;
import com.heachi.external.clients.oauth2.naver.response.NaverProfileResponse;
import com.heachi.external.clients.oauth2.naver.response.NaverTokenResponse;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class OAuthNaverAdapterTest extends TestConfig {

    @Autowired
    private OAuthNaverAdapter oAuthNaverAdapter;

    @Autowired
    private NaverURLBuilder naverURLBuilder;

    @Test
    @DisplayName("네이버 토큰 API에 정상적인 요청을 보내면, access_token이 발행된다.")
    void naverAdapterGetToken() {
        // given
        MockNaverTokenClients naverTokenClients = new MockNaverTokenClients();
        MockNaverProfileClients naverProfileClients = new MockNaverProfileClients();
        OAuthNaverAdapter oAuthNaverAdapter = new OAuthNaverAdapter(naverTokenClients, naverProfileClients);

        // when
        String accessToken = oAuthNaverAdapter.getToken("myUrl");

        // then
        System.out.println("accessToken = " + accessToken);
        assertThat(accessToken).isEqualTo("accessToken");
    }

/*    @Test
    @DisplayName("네이버 토큰 API 요청 중 예외가 발생하면 Adapter에서 OAuthException으로 예외 처리를 한다.")
    void naverAdapterGetTokenWhenApiThrowsException() {
        // given
        String tokenUrl = naverURLBuilder.token("DeniedToken", "state");

        // when
        assertThatThrownBy(() -> oAuthNaverAdapter.getToken(tokenUrl))
                .isInstanceOf(OAuthException.class);
    }*/


    @Test
    @DisplayName("네이버 프로필 API에 정상적인 요청을 보내면, 사용자 정보를 가져온다.")
    void naverAdapterGetProfile() {
        // given
        MockNaverTokenClients naverTokenClients = new MockNaverTokenClients();
        MockNaverProfileClients naverProfileClients = new MockNaverProfileClients();
        OAuthNaverAdapter oAuthNaverAdapter = new OAuthNaverAdapter(naverTokenClients, naverProfileClients);

        // when
        OAuthResponse profile = oAuthNaverAdapter.getProfile("accessToken");

        // then
        assertThat(profile)
                .extracting("platformId", "platformType", "email", "name", "profileImageUrl")
                .contains("123", UserPlatformType.NAVER, "hgd@naver.com", "hgd", "naver.com");
    }

//    @Test
//    @DisplayName("네이버 프로필 API에 요청 중 예외가 발생하면 Adapter에서 OAuthException으로 예외 처리를 한다.")
//    void naverAdapterGetProfileWhenApiThrowsException() {
//        //given
//        String accessToken = "DeniedToken";
//
//        // when
//        assertThatThrownBy(() -> oAuthNaverAdapter.getProfile(accessToken))
//                .isInstanceOf(OAuthException.class);
//    }

    static class MockNaverTokenClients implements NaverTokenClients {
        @Override
        public NaverTokenResponse getToken(URI uri) {
            return new NaverTokenResponse("accessToken");
        }
    }

    static class MockNaverProfileClients implements NaverProfileClients {
        @Override
        public NaverProfileResponse getProfile(String headers) {
            return new NaverProfileResponse(new NaverProfileResponse.NaverAccount(
                    "123", "hgd@naver.com", "hgd", "naver.com"
            ));
        }
    }


}