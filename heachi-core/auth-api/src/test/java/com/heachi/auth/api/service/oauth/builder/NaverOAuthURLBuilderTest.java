package com.heachi.auth.api.service.oauth.builder;

import com.heachi.auth.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NaverOAuthURLBuilderTest extends TestConfig {

    @Autowired
    private NaverURLBuilder urlBuilder;

    @Test
    @DisplayName("Naver - authorize URL이 성공적으로 만들어진다.")
    void authorize() {
        // given
        String state = "sessionID";

        // when
        String authorize = urlBuilder.authorize(state);

        // then
        System.out.println("authorize = " + authorize);
    }

    @Test
    @DisplayName("Naver - token URL이 성공적으로 만들어진다.")
    void token() {
        // given
        String code = "NaverCode";

        // when
        String token = urlBuilder.token(code);

        // then
        System.out.println("token = " + token);
    }

    @Test
    @DisplayName("Naver - profile URL이 성공적으로 만들어진다.")
    void profile() {
        // given

        // when
        String profile = urlBuilder.profile();

        //then
        System.out.println("profile = " + profile);
    }
}