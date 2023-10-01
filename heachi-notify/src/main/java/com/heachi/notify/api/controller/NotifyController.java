package com.heachi.notify.api.controller;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.notify.NotifyException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.notify.api.controller.request.NotifyRegistRequest;
import com.heachi.notify.api.service.auth.AuthService;
import com.heachi.notify.api.service.notify.request.NotifyServiceRegistRequest;
import com.heachi.notify.api.service.notify.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class NotifyController {

    private final NotifyService notifyService;
    private final AuthService authService;

    /**
     * 알림 받기
     */
    @GetMapping(value = "/")
    public Flux<JsonResult> receive(
            @RequestHeader(value = "Authorization", required = false, defaultValue = "token") String headers,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        return authService.getUserId(headers)
                .flatMapMany(sendUserId -> notifyService.receive(sendUserId, page))
                .subscribeOn(Schedulers.boundedElastic());  // publisher의 스케줄러를 boundedElastic으로 변경
    }

    /**
     * 알림 추가하기
     */
    @PostMapping("/")
    public Mono<JsonResult> registNotify(
            @RequestHeader(value = "Authorization", required = false, defaultValue = "token") String headers,
            @RequestBody NotifyRegistRequest request) {

        return authService.getUserId(headers)
                .flatMap(sendUserId ->
                    request.getReceiveUserIds().stream().filter(receiverId -> receiverId.equals(sendUserId))
                            .findFirst()
                            .map(id -> Mono.error(new NotifyException(ExceptionMessage.NOTIFY_DUPLICATE_ID)))
                            .orElseGet(() -> Mono.just(sendUserId))
                )
                .flatMap(sendUserId -> notifyService
                        .registNotify(NotifyServiceRegistRequest.of(request, sendUserId.toString()))
                        .thenReturn(JsonResult.successOf()))
                .subscribeOn(Schedulers.boundedElastic());  // publisher의 스케줄러를 boundedElastic으로 변경
    }

    /**
     * 사용자 알림 읽기 이벤트
     */
    @GetMapping("/read/{notifyId}")
    public Mono<JsonResult> readNotify(
            @RequestHeader(value = "Authorization", required = false, defaultValue = "token") String headers,
            @PathVariable("notifyId") String notifyId) {

        return authService.getUserId(headers)
                .flatMap(userId -> notifyService.readNotify(userId, notifyId))
                .onErrorMap(throwable -> new NotifyException(ExceptionMessage.NOTIFY_NOT_FOUND))
                .map(notify -> JsonResult.successOf())
                .subscribeOn(Schedulers.boundedElastic());
    }
}
