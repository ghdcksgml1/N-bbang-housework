package com.heachi.auth.api.service.oauth.builder;

import com.heachi.auth.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KakaoOAuthURLBuilderTest extends TestConfig {

    @Autowired
    private KakaoURLBuilder urlBuilder;

    @Test
    @DisplayName("authorize URL이 성공적으로 만들어진다.")
    void authorizeURLSuccesBuild() {
        // given
        String state = "CsrfDetecting";

        // when
        String authorize = urlBuilder.authorize(state);

        // then
        System.out.println("authorize = " + authorize);
    }

    @Test
    @DisplayName("token URL이 성공적으로 만들어진다.")
    void tokenURLSuccessBuild() {
        // given
        String code = "1bstRNDxht6X3Xi90CexKCqgSQUZWcvoez_WK_8gOOxFceG2HizHOloPJKv1VM0WUeJIugo9c5sAAAGJ2JZu9A";
        String state = "state";

        // when
        String token = urlBuilder.token(code, state);

        // then
        System.out.println("token = " + token);
    }

    @Test
    @DisplayName("profile URL이 성공적으로 만들어진다.")
    void profileURLSuccessBuild() {
        // given


        // when
        String profile = urlBuilder.profile();

        // then
        System.out.println("profile = " + profile);
    }
}