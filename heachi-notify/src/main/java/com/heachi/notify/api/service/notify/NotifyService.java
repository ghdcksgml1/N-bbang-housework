package com.heachi.notify.api.service.notify;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.mongo.define.notify.Notify;
import com.heachi.mongo.define.notify.repository.NotifyRepository;
import com.heachi.notify.api.service.notify.request.NotifyServiceRegistRequest;
import com.heachi.notify.api.service.notify.response.NotifyServiceReceiverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
                .flatMap(notify -> {
                    NotifyServiceReceiverResponse nsrr = NotifyServiceReceiverResponse.of(notify);

                    return Flux.just(JsonResult.successOf(nsrr));
                })
                .timeout(Duration.ofSeconds(30))
                .onErrorReturn(TimeoutException.class, JsonResult.failOf("Timeout"));   // 30초가 지나면 타임아웃
    }

    public Mono<Notify> registNotify(NotifyServiceRegistRequest request) {
        return notifyRepository.save(request.toEntity());
    }
}
