package com.heachi.notify.api.service.auth;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.jwt.JwtException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.clients.auth.AuthClients;
import com.heachi.external.clients.auth.response.UserInfoResponse;
import com.heachi.notify.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthServiceTest extends TestConfig {

    private AuthService authService;
    private MockAuthClients mockAuthClients;

    @BeforeEach
    void setUp() {
        mockAuthClients = new MockAuthClients();
        authService = new AuthService(mockAuthClients);
    }

    @Test
    @DisplayName("사용자가 이상한 토큰으로 사용자 인증을 요구하면 에러를 뱉는다.")
    void malformedTokenWhenAuthServerRequest() {
        // given
        String malformedToken = "malformedToken";

        Mono<String> mono = authService.getUserId(malformedToken).log();

        // when
        StepVerifier.create(mono)
                // then
                .expectSubscription()
                .expectError()
                .verify();
    }

    @Test
    @DisplayName("올바른 토큰을 보냈을때 유저 정보를 리턴한다.")
    void getUserInfoSuccess() {
        // given
        String token = "successToken";

        Mono<String> mono = authService.getUserId(token).log();

        // when
        StepVerifier.create(mono)
                // then
                .expectSubscription()
                .expectNextMatches(userId -> {
                    assertThat(userId).isEqualTo("role");

                    return true;
                })
                .verifyComplete();
    }

    private class MockAuthClients implements AuthClients {

        @Override
        public Mono<JsonResult<UserInfoResponse>> getUserInfo(String headers) {
            if (headers.equals("malformedToken")) {
                return Mono.error(new JwtException(ExceptionMessage.JWT_MALFORMED));
            } else {
                return Mono.just(JsonResult.successOf(
                        new UserInfoResponse("id", "type", "role", "name")
                ));
            }
        }
    }


}