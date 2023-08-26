package com.heachi.notify.api.service.notify.request;

import com.heachi.mongo.define.notify.Notify;
import com.heachi.mongo.define.notify.constant.NotifyType;
import com.heachi.notify.api.controller.request.NotifyRegistRequest;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Getter
public class NotifyServiceRegistRequest {
    private String sendUserId;                                          // 알림을 보낸 유저 아이디
    private List<String> receiveUserIds = new ArrayList<>();            // 알림을 받는 아이디
    private NotifyType type;                                            // 알림 종류
    private String message;                                             // 알림 내용
    private String generatedUrl;                                        // 알림의 근원지
    private String url;                                                 // 알림 클릭 시 이동할 주소

    @Builder
    private NotifyServiceRegistRequest(String sendUserId, List<String> receiveUserIds, NotifyType type, String message
            , String generatedUrl, String url) {
        this.sendUserId = sendUserId;
        this.receiveUserIds = receiveUserIds;
        this.type = type;
        this.message = message;
        this.generatedUrl = generatedUrl;
        this.url = url;
    }

    public static NotifyServiceRegistRequest of(NotifyRegistRequest request, String sendUserId) {
        return NotifyServiceRegistRequest.builder()
                .sendUserId(sendUserId)
                .receiveUserIds(request.getReceiveUserIds())
                .type(request.getType())
                .message(request.getMessage())
                .generatedUrl(request.getGeneratedUrl())
                .url(request.getUrl())
                .build();
    }

    public Notify toEntity() {
        return Notify.builder()
                .sendUserId(this.getSendUserId())
                .receiveUserIds(this.receiveUserIds)
                .type(this.type)
                .message(this.message)
                .generatedUrl(this.generatedUrl)
                .createdTime(LocalDateTime.now())
                .checkedTime(new HashMap<>())
                .checked(new HashSet<>())
                .url(this.url)
                .build();
    }
}
