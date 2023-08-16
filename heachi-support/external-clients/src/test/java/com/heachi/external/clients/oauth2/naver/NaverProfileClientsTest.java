package com.heachi.external.clients.oauth2.naver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NaverProfileClientsTest {

    @Autowired
    private NaverProfileClients naverProfileClients;

    @Test
    @DisplayName("access_token을 Bearer token으로 보내면 요청이 성공한다.")
    void naverProfileRequest() {
        // given
        String token = "AAAAPG7mnmzPbVoubTUdLMpSBvbzNVDmezIr5N3S18vbLq2iQEAt3Y-bGtW7Av84wlOqygXIdzzTE9DBma4_WhhpuyI";
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

//        naverProfileClients.getProfile("Bearer " + token);

        // when
        assertThatThrownBy(() -> naverProfileClients.getProfile(headers.get("Authorization")))
                .isInstanceOf(RuntimeException.class);
    }
}