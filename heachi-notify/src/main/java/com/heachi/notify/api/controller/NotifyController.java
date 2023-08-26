package com.heachi.notify.api.controller;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.oauth.OAuthException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.clients.auth.AuthClients;
import com.heachi.notify.api.controller.request.NotifyRegistRequest;
import com.heachi.notify.api.service.auth.AuthService;
import com.heachi.notify.api.service.notify.request.NotifyServiceRegistRequest;
import com.heachi.notify.api.service.notify.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.ConnectException;
import java.time.Duration;

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class NotifyController {

    private final NotifyService notifyService;
    private final AuthService authService;

    @GetMapping(value = "/", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<JsonResult> receive(@RequestHeader(value = "Authorization", required = false, defaultValue = "token") String headers) {

        return authService.getUserId(headers)
                .flatMapMany(sendUserId -> notifyService.receive(sendUserId))
                .subscribeOn(Schedulers.boundedElastic());  // publisher의 스케줄러를 boundedElastic으로 변경
    }

    @PostMapping("/")
    public Mono<JsonResult> registNotify(
            @RequestHeader(value = "Authorization", required = false, defaultValue = "token") String headers,
            @RequestBody NotifyRegistRequest request) {
        return authService.getUserId(headers)
                .flatMap(sendUserId -> notifyService
                        .registNotify(NotifyServiceRegistRequest.of(request, sendUserId))
                        .thenReturn(JsonResult.successOf()))
                .subscribeOn(Schedulers.boundedElastic());  // publisher의 스케줄러를 boundedElastic으로 변경
    }
}
