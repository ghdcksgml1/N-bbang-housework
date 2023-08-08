package com.heachi.notify.api.controller;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.notify.api.controller.request.NotifyRegistRequest;
import com.heachi.notify.api.service.request.NotifyServiceRegistRequest;
import com.heachi.notify.api.service.response.NotifyServiceReceiverResponse;
import com.heachi.notify.api.service.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class NotifyController {

    private final NotifyService notifyService;

    @GetMapping(value = "/{receiverId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NotifyServiceReceiverResponse> receive(@PathVariable("receiverId") String receiverId) {

        return notifyService.receive(receiverId, 30);
    }

    @PostMapping("/")
    public JsonResult registNotify(@RequestBody NotifyRegistRequest request) {
        notifyService.registNotify(NotifyServiceRegistRequest.of(request));
        return JsonResult.successOf();
    }
}
