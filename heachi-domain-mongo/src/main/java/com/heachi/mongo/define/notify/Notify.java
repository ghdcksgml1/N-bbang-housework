package com.heachi.mongo.define.notify;

import com.heachi.mongo.define.notify.constant.NotifyType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@ToString
@Document(collection = "notify")
@Slf4j
public class Notify {
    @Id
    private String id;

    private String sendUserId;                                          // 알림을 보낸 유저 아이디
    private List<String> receiveUserIds = new ArrayList<>();            // 알림을 받는 아이디
    private NotifyType type;                                            // 알림 종류
    private String message;                                             // 알림 내용
    private String url;                                                 // 알림 클릭 시 이동할 주소
    private LocalDateTime createdTime = LocalDateTime.now();            // 알림 발생 시간
    private Map<String, LocalDateTime> checkedTime = new HashMap<>();   // 알림 확인 시간
    private Set<String> checked = new HashSet<>();                      // 알림을 확인했는지 안했는지

    @Builder
    private Notify(String sendUserId, List<String> receiveUserIds, NotifyType type, String message, String url,
                   LocalDateTime createdTime, Map<String, LocalDateTime> checkedTime, Set<String> checked) {
        this.sendUserId = sendUserId;
        this.receiveUserIds = receiveUserIds;
        this.type = type;
        this.message = message;
        this.url = url;
        this.createdTime = createdTime;
        this.checkedTime = checkedTime;
        this.checked = checked;
    }

    public void receiverUserCheckedNotify(String receiverUserId) {
        // receiver에 해당 사용자가 있는지 확인한다.
        this.receiveUserIds.stream()
                .filter(id -> id.equals(receiverUserId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error(">>>> ReceiverIds에 해당 사용자가 존재하지 않습니다 : {}", receiverUserId);
                    throw new IllegalArgumentException();
                });

        // 있다면, 체크표시
        checked.add(receiverUserId);
        checkedTime.put(receiverUserId, LocalDateTime.now());
    }
}
