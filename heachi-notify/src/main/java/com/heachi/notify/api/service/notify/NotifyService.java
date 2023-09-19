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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotifyService {

    private final NotifyRepository notifyRepository;

    public Flux<JsonResult> receive(String receiverId, int page) {
        return notifyRepository.findNotifyByReceiveUserIdsPaging(receiverId, page)
                .flatMap(notify -> Flux.just(JsonResult.successOf(NotifyServiceReceiverResponse.of(notify, receiverId))));
    }

    public Mono<Notify> registNotify(NotifyServiceRegistRequest request) {
        return notifyRepository.save(request.toEntity());
    }

    public Mono<Notify> readNotify(String userId, String notifyId) {
        return notifyRepository.findNotifyByIdWhereReceiveUserIdsIn(userId, notifyId)
                .switchIfEmpty(Mono.error(new NotifyException(ExceptionMessage.NOTIFY_NOT_FOUND)))
                .flatMap(notify -> {
                    // 이미 알림 확인했는지 체크
                    if (!notify.getChecked().contains(userId)) {
                        notify.receiverUserCheckedNotify(userId); // 알림 확인 체크

                        return notifyRepository.save(notify);
                    } else {
                        return Mono.just(notify);
                    }
                });
    }
}
