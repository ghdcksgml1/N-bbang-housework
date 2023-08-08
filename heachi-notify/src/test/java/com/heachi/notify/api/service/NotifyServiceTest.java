package com.heachi.notify.api.service;

import com.heachi.mongo.define.notify.Notify;
import com.heachi.mongo.define.notify.repository.NotifyRepository;
import com.heachi.notify.TestConfig;
import com.heachi.notify.api.service.response.NotifyServiceReceiverResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotifyServiceTest extends TestConfig {

    @Autowired
    private NotifyRepository notifyRepository;

    @Autowired
    private NotifyService notifyService;

    @AfterEach
    void tearDown() {
        notifyRepository.deleteAll();
    }

    @Test
    @Disabled
    @DisplayName("구독 도중 객체가 추가되었을때 값이 반영이 되어있어야한다.")
    void addNotifyWhenSubscribeOn() {
        // given
        Notify notify1 = Notify.builder()
                .sendUserId("홍찬희1")
                .receiveUserIds(List.of("ghdcksgml1", "ghdcksgml2", "ghdcksgml3"))
                .build();
        Notify notify2 = Notify.builder()
                .sendUserId("홍찬희1")
                .receiveUserIds(List.of("ghdcksgml1", "ghdcksgml2", "ghdcksgml3"))
                .build();

        // when
        Flux<NotifyServiceReceiverResponse> responseFlux = notifyService.receive("ghdcksgml1", 1);
        Flux<Notify> notifyFlux = notifyRepository.saveAll(List.of(notify1, notify2));

        // then
        StepVerifier.create(responseFlux)
                .expectNextMatches(response -> {
                    assertThat(response.getSendUserId()).isEqualTo("홍찬희1");

                    return true;
                })
                .verifyComplete();
    }
}