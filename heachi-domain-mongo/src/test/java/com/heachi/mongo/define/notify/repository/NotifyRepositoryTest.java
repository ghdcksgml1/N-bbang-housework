package com.heachi.mongo.define.notify.repository;

import com.heachi.mongo.TestConfig;
import com.heachi.mongo.define.notify.Notify;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotifyRepositoryTest extends TestConfig {

    @Autowired
    private NotifyRepository notifyRepository;

    @AfterEach
    void tearDown() {
        notifyRepository.deleteAll().subscribe();
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
    @DisplayName("ReceiveUserId를 넣고, 페이지를 넣으면 해당 페이지의 Notify가 10개가 나온다.")
    void selectNotifyWhereReceiveUserIdLimit10Pagination() {
        // given
        List<Notify> list = new ArrayList<>();
        for (int i=0; i<10; i++) {
            Notify notify = Notify.builder()
                    .sendUserId("ghdcksgml")
                    .receiveUserIds(List.of("ghdcksgml1"))
                    .createdTime(LocalDateTime.now())
                    .build();
            list.add(notify);
        }


        Flux<Notify> flux1 = notifyRepository.saveAll(list);
        Flux<Notify> flux2 = notifyRepository.findNotifyByReceiveUserIdsPaging("ghdcksgml1", 0);


        // when
        StepVerifier.create(Flux.concat(flux1, flux2).log())
                // then
                .expectSubscription()
                .expectNextCount(10)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    @DisplayName("notifyId가 일치하고, receiverUserIds에 나의 요청하는 아이디가 있다면, Notify를 가져온다.")
    void selectNotifyWhereNotifyIdAndReceiverUserIdsIn() {
        // given
        Notify notify = Notify.builder()
                .sendUserId("ghdcksgml")
                .receiveUserIds(List.of("ghdcksgml1"))
                .createdTime(LocalDateTime.now())
                .build();

        Notify savedNotify = notifyRepository.save(notify).block();

        // when
        StepVerifier.create(notifyRepository.findNotifyByIdWhereReceiveUserIdsIn("ghdcksgml1", savedNotify.getId()))
                // then
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("notifyId가 일치하지만, receiverUserIds에 요청하는 아이디가 존재하지 않는다면, 권한이 없으므로 아무것도 리턴되지 않는다.")
    void selectNotifyWhereNotifyIdAndReceiverUserIdsInNotMatching() {
        // given
        Notify notify = Notify.builder()
                .sendUserId("ghdcksgml")
                .receiveUserIds(List.of("ghdcksgml1"))
                .createdTime(LocalDateTime.now())
                .build();

        Notify savedNotify = notifyRepository.save(notify).block();

        // when
        StepVerifier.create(notifyRepository.findNotifyByIdWhereReceiveUserIdsIn("ghdcksgml", savedNotify.getId()))
                // then
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }
}