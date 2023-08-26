package com.heachi.notify.config.advice;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.notify.TestConfig;
import com.heachi.notify.api.controller.NotifyController;
import com.heachi.notify.api.service.auth.AuthService;
import com.heachi.notify.api.service.notify.NotifyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
@WebFluxTest(NotifyController.class)
class GlobalExceptionHandlerTest extends TestConfig {

    @MockBean
    private AuthService authService;

    @MockBean
    private NotifyService notifyService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Advice가 런타임에 에러가 발생하면 가져와 JsonResult.failOf()로 변환한다.")
    void exceptionHandlerCatchException() {
        // given
        BDDMockito.given(authService.getUserId(anyString()))
                .willThrow(new RuntimeException());


        // when
        webTestClient.get().uri("/notify/").accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(JsonResult.class)
                .getResponseBody()
                // then
                .as(StepVerifier::create)
                .assertNext(jsonResult -> assertThat(jsonResult.getResCode()).isEqualTo(400))
                .thenCancel()
                .verify();
    }

}