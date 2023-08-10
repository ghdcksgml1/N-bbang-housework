package com.heachi.external.clients.oauth2.kakao;

import com.heachi.external.TestConfig;
import com.heachi.external.clients.oauth2.kakao.response.KakaoTokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class KakaoTokenClientsTest extends TestConfig {

    @Autowired
    private KakaoTokenClients kakaoTokenClients;

    @Test
    @DisplayName("kakao token을 보내면, 서버에서 엑세스 토큰을 내려준다.")
    void kakaoTokenRequest() {
        // given
        String uri = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id=21a73e6bf7b52ed4f56e4f3ad19705d0&client_secret=f20Pj13NkgGlMY8VpooE6KAwxHKW7zCc&redirect_uri=http://localhost:8080/auth/KAKAO/login&code=vxJY9Aczvfm_V0CowiGfSgyxDH2UtTmYKOzP2m9S83xL5mywRu_cUrVCTuKRfvru9Y1nngo9dZwAAAGJ2UMuCA";

        // when
        assertThatThrownBy(() -> kakaoTokenClients.getToken(URI.create(uri)))
                // then
                .isInstanceOf(Exception.class);
    }
//    {
//        "error": "invalid_grant",
//            "error_description": "authorization code not found for code=uLHvf9Enl3F5XLw-mERF_4v-SQS9nM_vjHpubrAYF2eyZi64y9YphA-0GFxaDPmdaVs6Iwo9cxgAAAGJ2PaQZA",
//            "error_code": "KOE320"
//    }
}