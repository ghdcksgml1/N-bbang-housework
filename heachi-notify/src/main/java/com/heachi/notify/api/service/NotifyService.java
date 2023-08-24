package com.heachi.notify.api.service;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.mongo.define.notify.Notify;
import com.heachi.mongo.define.notify.repository.NotifyRepository;
import com.heachi.notify.api.controller.request.NotifyRegistRequest;
import com.heachi.notify.api.service.request.NotifyServiceRegistRequest;
import com.heachi.notify.api.service.response.NotifyServiceReceiverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class NotifyService {

    private final NotifyRepository notifyRepository;

    public Flux<JsonResult> receive(String receiverId) {
        return notifyRepository.findByReceiveUserIds(receiverId)
                .flatMap(notify -> {
                    NotifyServiceReceiverResponse nsrr = NotifyServiceReceiverResponse.of(notify);

                    return Flux.just(JsonResult.successOf(nsrr));
                });
    }

    public void registNotify(NotifyServiceRegistRequest request) {
        notifyRepository.save(request.toEntity()).subscribe();
    }
}
