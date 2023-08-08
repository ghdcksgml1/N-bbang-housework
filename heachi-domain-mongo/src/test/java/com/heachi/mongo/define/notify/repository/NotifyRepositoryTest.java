package com.heachi.mongo.define.notify.repository;

import com.heachi.mongo.TestConfig;
import com.heachi.mongo.define.notify.Notify;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotifyRepositoryTest extends TestConfig {

    @Autowired
    private NotifyRepository notifyRepository;

    @AfterEach
    void tearDown() {
        notifyRepository.deleteAll();
    }

    @Test
    @DisplayName("Notify 객체를 만들어 저장할 수 있다.")
    void saveTest() {
        // given
        Notify notify = Notify.builder()
                .sendUserId("홍찬희")
                .build();

        // when
        notifyRepository.save(notify)
                // then
                .as(StepVerifier::create)
                .expectNextMatches(savedNotify -> {
                    assertThat(savedNotify.getSendUserId()).isEqualTo("홍찬희");

                    return true;
                })
                .verifyComplete();
    }

    @Test
    @Disabled
    @DisplayName("ReceiveUserIds 리스트에 ReceiveUserId가 존재하면 해당 Notify를 반환한다.")
    void findByReceiveUserIds() {
        // given
        Notify notify = Notify.builder()
                .sendUserId("홍찬희")
                .receiveUserIds(List.of("ghdcksgml1", "ghdcksgml2", "ghdcksgml3"))
                .build();

        // when
        Mono<Notify> save = notifyRepository.save(notify);
        Flux<Notify> ghdcksgml1 = notifyRepository.findByReceiveUserIds("ghdcksgml1");
        assertThat(ghdcksgml1.collectList().block()).isEqualTo(notify.getSendUserId());
    }
}