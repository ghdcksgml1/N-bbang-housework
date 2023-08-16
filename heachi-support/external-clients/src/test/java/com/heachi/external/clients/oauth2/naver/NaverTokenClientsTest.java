package com.heachi.external.clients.oauth2.naver;

import com.heachi.external.TestConfig;
import com.heachi.external.clients.oauth2.naver.response.NaverTokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NaverTokenClientsTest extends TestConfig {

    @Autowired
    private NaverTokenClients naverTokenClients;

    @Test
    @DisplayName("네이버 인가 code를 보내면 서버에서 엑세스 토큰을 내려준다.")
    void naverTokenRequest() {
        // given
        String uri = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=gUuvVfgWzKNtZhqUqIaV&client_secret=ZyVnAvpVhm&code=xxx&state=xxx";

        // when
        String access_token = naverTokenClients.getToken(URI.create(uri)).getAccess_token();
//        System.out.println("access_token = " + access_token);

        //then
        assertNull(access_token);
    }
}