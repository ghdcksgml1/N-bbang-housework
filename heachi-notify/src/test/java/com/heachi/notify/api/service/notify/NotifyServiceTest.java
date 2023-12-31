package com.heachi.notify.api.service.notify;

import com.heachi.mongo.define.notify.Notify;
import com.heachi.mongo.define.notify.constant.NotifyType;
import com.heachi.mongo.define.notify.repository.NotifyRepository;
import com.heachi.notify.TestConfig;
import com.heachi.notify.api.service.notify.request.NotifyServiceRegistRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class NotifyServiceTest extends TestConfig {

    @Autowired
    private NotifyRepository notifyRepository;

    @Autowired
    private NotifyService notifyService;

    @BeforeEach
    void setUp() {
        notifyRepository.deleteAll().block();
    }


//    @Test
//    @DisplayName("구독하면, 30초가 커넥션이 유지되고, 30초가 지나면 커넥션은 끊긴다.")
//    void connection30secondsWhenClientSubscribe() {
//        // given
//
//        // when
//        StepVerifier.withVirtualTime(() -> notifyService.receive("2954438047",0))
//                // then
//                .expectSubscription()               // 구독이 시작되고 나서
//                .expectNoEvent(Duration.ofSeconds(30))  // 30초간 아무런 응답이 없으면
//                .expectNextMatches(jsonResult -> {  // 에러를 뱉음
//                    assertThat(jsonResult.getResCode()).isEqualTo(400);
//                    assertThat(jsonResult.getResMsg()).isEqualTo("Timeout");
//
//                    return true;
//                })
//                .verifyComplete();
//    }
//
//    @Test
//    @DisplayName("구독 도중 객체가 추가되었을때 값이 곧바로 반영된다.")
//    void addNotifyWhenSubscribeOn() {
//        // given
//        Notify notify1 = Notify.builder()
//                .sendUserId("ghdcksgml1")
//                .receiveUserIds(List.of("ghdcksgml1", "ghdcksgml2", "ghdcksgml3"))
//                .type(NotifyType.NOTE)
//                .createdTime(LocalDateTime.now())
//                .checkedTime(new HashMap<>())
//                .checked(new HashSet<>())
//                .build();
//        Notify notify2 = Notify.builder()
//                .sendUserId("ghdcksgml2")
//                .receiveUserIds(List.of("ghdcksgml1", "ghdcksgml2", "ghdcksgml3"))
//                .type(NotifyType.NOTE)
//                .createdTime(LocalDateTime.now())
//                .checkedTime(new HashMap<>())
//                .checked(new HashSet<>())
//                .build();
//        Notify notify3 = Notify.builder()
//                .sendUserId("ghdcksgml3")
//                .receiveUserIds(List.of("ghdcksgml1", "ghdcksgml2", "ghdcksgml3"))
//                .type(NotifyType.NOTE)
//                .createdTime(LocalDateTime.now())
//                .checkedTime(new HashMap<>())
//                .checked(new HashSet<>())
//                .build();
//
//        notifyRepository.save(notify1).block(); // 매칭되는 아이디가 없으면 Flux가 실행이 안되기 때문에 임의의 데이터를 저장.
//
//        // when
//        StepVerifier.withVirtualTime(() -> {
//                    Flux<Notify> event = Mono.delay(Duration.ofSeconds(2)).thenMany(notifyRepository.saveAll(List.of(notify2, notify3)));
//                    Flux<JsonResult> flux = notifyService.receive("ghdcksgml1",0);
//
//                    return Flux.merge(event, flux).log();
//                })
//                // then
//                .expectSubscription()
//                .expectNextCount(1)                     // 임의의 데이터 1개
//                .expectNoEvent(Duration.ofSeconds(2))   // 2초뒤 2개 저장됨
//                .expectNextCount(4)                     // repository.save()가 2번 호출됨, notifyService.receive()에서 2개의 데이터를 바로 받음
//                .expectNoEvent(Duration.ofSeconds(30))  // idle 상태가 30초 동안 지속되면
//                .expectNextMatches(o -> {
//                    if (o instanceof JsonResult) {
//                        assertThat(((JsonResult<?>) o).getResCode()).isEqualTo(400);
//
//                        return true;
//                    }
//                    return false;
//                })
//                .verifyComplete();
//    }

    @Test
    @DisplayName("올바른 데이터를 넣어주면, Notify에 저장되고, ID도 부여된다.")
    void registNotify() {
        // given
        NotifyServiceRegistRequest notify = NotifyServiceRegistRequest.builder()
                .sendUserId("ghdcksgml")
                .receiveUserIds(List.of("ghdcksgml1", "ghdcksgml2", "ghdcksgml3"))
                .type(NotifyType.NOTE)
                .build();

        Mono<Notify> notifyMono = notifyService.registNotify(notify) // notify 등록 후
                .flatMap(n -> notifyRepository.findById(n.getId())); // Id로 조회하기

        // when
        StepVerifier.create(notifyMono.log())
                // then
                .expectSubscription()
                .expectNextMatches(n -> {
                    assertThat(n.getId()).isNotNull();
                    assertThat(n.getSendUserId()).isEqualTo("ghdcksgml");
                    assertThat(n.getType()).isEqualTo(NotifyType.NOTE);

                    return true;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("자신이 Receiver로 지정되어 있는 알림을 읽으면 Checked가 true가 된다.")
    void notifyReadEvent() {
        // given
        Notify notify = Notify.builder()
                .sendUserId("ghdcksgml")
                .receiveUserIds(List.of("ghdcksgml1"))
                .checked(new HashSet<>())
                .checkedTime(new HashMap<>())
                .createdTime(LocalDateTime.now())
                .build();
        Notify savedNotify = notifyRepository.save(notify).block();

        Mono<Notify> mono = notifyService.readNotify("ghdcksgml1", savedNotify.getId());

        // when
        StepVerifier.create(mono)
                // then
                .expectSubscription()
                .assertNext(notify1 -> assertThat(notify1.getChecked()).contains("ghdcksgml1"))
                .verifyComplete();
    }


    @Test
    @DisplayName("findNotifyByIdWhereReceiveUserIdsIn에서 찾은 값이 없다면, NOTIFY_NOT_FOUND 에러를 발생시킨다.")
    void notifyReadEventFailWhenFindQueryResultIsNull() {
        // given
        Notify notify = Notify.builder()
                .sendUserId("ghdcksgml")
                .receiveUserIds(List.of("ghdcksgml1"))
                .createdTime(LocalDateTime.now())
                .build();
        Notify savedNotify = notifyRepository.save(notify).block();

        Mono<Notify> mono = notifyService.readNotify("UnKnownUser", savedNotify.getId());

        // when
        StepVerifier.create(mono)
                // then
                .expectSubscription()
                .expectError()
                .verify();
    }
}