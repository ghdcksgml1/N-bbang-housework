package com.heachi.notify.api.controller.request;

import com.heachi.mongo.define.notify.constant.NotifyType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class NotifyRegistRequest {
    private String sendUserId;                                          // 알림을 보낸 유저 아이디
    private List<String> receiveUserIds = new ArrayList<>();            // 알림을 받는 아이디
    private NotifyType type;                                            // 알림 종류
    private String message;                                             // 알림 내용
    private String generatedUrl;                                        // 알림의 근원지
    private String url;                                                 // 알림 클릭 시 이동할 주소
}
