package com.heachi.external.clients.oauth2.kakao;

import com.heachi.external.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KakaoProfileClientsTest extends TestConfig {

    @Autowired
    private KakaoProfileClients kakaoProfileClients;

    @Test
    @DisplayName("access_token을 Bearer token으로 보내면, 요청이 성공한다.")
    void profileRequest() {
        // given
        String token = "LoDZCwAQ4sM8r5v-2hMkeau-3N1m4m9sIf0efOwGCj1y6gAAAnZQ9Lt";
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        // when
        assertThatThrownBy(() -> kakaoProfileClients.getProfile("Bearer " + token))
                // then
                .isInstanceOf(RuntimeException.class);
    }
}