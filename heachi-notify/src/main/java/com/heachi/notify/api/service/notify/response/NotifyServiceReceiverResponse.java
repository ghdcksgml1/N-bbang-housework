package com.heachi.notify.api.service.notify.response;

import com.heachi.admin.common.utils.DateDistance;
import com.heachi.mongo.define.notify.Notify;
import com.heachi.mongo.define.notify.constant.NotifyType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@ToString
public class NotifyServiceReceiverResponse {
    private String sendUserId;                                          // 알림을 보낸 유저 아이디
    private List<String> receiveUserIds = new ArrayList<>();            // 알림을 받는 아이디
    private NotifyType type;                                            // 알림 종류
    private String message;                                             // 알림 내용
    private String generatedUrl;                                        // 알림의 근원지
    private String url;                                                 // 알림 클릭 시 이동할 주소
    private LocalDateTime createdTime = LocalDateTime.now();            // 알림 발생 시간
    private Map<String, LocalDateTime> checkedTime = new HashMap<>();   // 알림 확인 시간
    private Set<String> checked = new HashSet<>();                      // 알림을 확인했는지 안했는지
    private String dateDistance;                                        // 얼마나 지난 알림인지

    @Builder
    private NotifyServiceReceiverResponse(String sendUserId, List<String> receiveUserIds, NotifyType type, String message
            , String generatedUrl, String url, LocalDateTime createdTime, Map<String, LocalDateTime> checkedTime
            , Set<String> checked, String dateDistance) {
        this.sendUserId = sendUserId;
        this.receiveUserIds = receiveUserIds;
        this.type = type;
        this.message = message;
        this.generatedUrl = generatedUrl;
        this.url = url;
        this.createdTime = createdTime;
        this.checkedTime = checkedTime;
        this.checked = checked;
        this.dateDistance = dateDistance;
    }

    public static NotifyServiceReceiverResponse of(Notify notify) {
        return NotifyServiceReceiverResponse.builder()
                .sendUserId(notify.getSendUserId())
                .receiveUserIds(notify.getReceiveUserIds())
                .type(notify.getType())
                .message(notify.getMessage())
                .generatedUrl(notify.getGeneratedUrl())
                .url(notify.getUrl())
                .createdTime(notify.getCreatedTime())
                .checkedTime(notify.getCheckedTime())
                .checked(notify.getChecked())
                .dateDistance(DateDistance.of(notify.getCreatedTime()))
                .build();
    }
}
