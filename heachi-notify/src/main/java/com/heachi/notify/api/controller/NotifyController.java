package com.heachi.notify.api.controller;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.clients.auth.AuthClients;
import com.heachi.notify.api.controller.request.NotifyRegistRequest;
import com.heachi.notify.api.service.request.NotifyServiceRegistRequest;
import com.heachi.notify.api.service.response.NotifyServiceReceiverResponse;
import com.heachi.notify.api.service.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class NotifyController {

    private final NotifyService notifyService;
    private final AuthClients authClients;

    @GetMapping(value = "/", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<JsonResult> receive(@RequestHeader(value = "Authorization") String headers) {
        System.out.println("headers = " + headers.substring(7));

        return authClients.getUserInfo(headers)
                .flatMapMany(userInfoResponseJsonResult -> {
                    if (userInfoResponseJsonResult.getResObj() == null) {
                        return Flux.just(JsonResult.failOf());
                    }

                    return notifyService.receive(userInfoResponseJsonResult.getResObj().getName());
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping("/")
    public JsonResult registNotify(@RequestBody NotifyRegistRequest request) {
        notifyService.registNotify(NotifyServiceRegistRequest.of(request));
        return JsonResult.successOf();
    }
}
