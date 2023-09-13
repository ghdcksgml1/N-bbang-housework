package com.heachi.notify.api.service.notify;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.notify.NotifyException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.mongo.define.notify.Notify;
import com.heachi.mongo.define.notify.repository.NotifyRepository;
import com.heachi.notify.api.service.notify.request.NotifyServiceRegistRequest;
import com.heachi.notify.api.service.notify.response.NotifyServiceReceiverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class NotifyService {

    private final NotifyRepository notifyRepository;

    public Flux<JsonResult> receive(String receiverId) {
        return notifyRepository.findByReceiveUserIds(receiverId)
                .doOnCancel(() -> System.out.println(">>>>>>>cancel!!!!!!!!!!"))
                .flatMap(notify -> Flux.just(JsonResult.successOf(NotifyServiceReceiverResponse.of(notify, receiverId))))
                .timeout(Duration.ofSeconds(30))
                .onErrorReturn(TimeoutException.class, JsonResult.failOf("Timeout"));   // 30초가 지나면 타임아웃
    }

    public Mono<Notify> registNotify(NotifyServiceRegistRequest request) {
        return notifyRepository.save(request.toEntity());
    }

    public Mono<JsonResult> readNotify(String userId, String notifyId) {
        return notifyRepository.findById(notifyId)
                .switchIfEmpty(Mono.error(new NotifyException(ExceptionMessage.NOTIFY_NOT_FOUND)))
                .flatMap(notify -> {
                    notify.receiverUserCheckedNotify(userId); // 알림 확인 체크
                    return notifyRepository.save(notify);
                })
                .map(notify -> JsonResult.successOf(NotifyServiceReceiverResponse.of(notify, userId)));
    }
}
