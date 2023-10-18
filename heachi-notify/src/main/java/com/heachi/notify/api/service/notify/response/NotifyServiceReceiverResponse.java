package com.heachi.notify.api.service.notify.response;

import com.heachi.admin.common.utils.DateDistance;
import com.heachi.mongo.define.notify.Notify;
import com.heachi.mongo.define.notify.constant.NotifyType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class NotifyServiceReceiverResponse {
    private String id;
    private String sendUserId;                                          // 알림을 보낸 유저 아이디
    private String receiveUserIds;                                      // 알림을 받는 아이디
    private NotifyType type;                                            // 알림 종류
    private String message;                                             // 알림 내용
    private String url;                                                 // 알림 클릭 시 이동할 주소
    private LocalDateTime createdTime;                                  // 알림 발생 시간
    private LocalDateTime checkedTime;                                  // 알림 확인 시간
    private boolean checked;                                             // 알림을 확인했는지 안했는지
    private String dateDistance;                                        // 얼마나 지난 알림인지

    @Builder
    public NotifyServiceReceiverResponse(String id, String sendUserId, String receiveUserIds, NotifyType type,
                                         String message, String url, LocalDateTime createdTime,
                                         LocalDateTime checkedTime, boolean checked, String dateDistance) {
        this.id = id;
        this.sendUserId = sendUserId;
        this.receiveUserIds = receiveUserIds;
        this.type = type;
        this.message = message;
        this.url = url;
        this.createdTime = createdTime;
        this.checkedTime = checkedTime;
        this.checked = checked;
        this.dateDistance = dateDistance;
    }

    public static NotifyServiceReceiverResponse of(Notify notify, String receiveUserId) {
        return NotifyServiceReceiverResponse.builder()
                .id(notify.getId())
                .sendUserId(notify.getSendUserId())
                .receiveUserIds(receiveUserId)
                .type(notify.getType())
                .message(notify.getMessage())
                .url(notify.getUrl())
                .createdTime(notify.getCreatedTime())
                .checkedTime(notify.getCheckedTime().get(receiveUserId))
                .checked(notify.getChecked().contains(receiveUserId))
                .dateDistance(DateDistance.of(notify.getCreatedTime()))
                .build();
    }
}
