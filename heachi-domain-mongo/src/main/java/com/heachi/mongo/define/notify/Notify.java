package com.heachi.mongo.define.notify;

import com.heachi.mongo.define.notify.constant.NotifyType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Document(collection = "notify")
public class Notify {
    @Id
    private String id;

    private String sendUserId;                                          // 알림을 보낸 유저 아이디
    private List<String> receiveUserIds = new ArrayList<>();            // 알림을 받는 아이디
    private NotifyType type;                                            // 알림 종류
    private String message;                                             // 알림 내용
    private String generatedUrl;                                        // 알림의 근원지
    private String url;                                                 // 알림 클릭 시 이동할 주소
    private LocalDateTime createdTime = LocalDateTime.now();            // 알림 발생 시간
    private Map<String, LocalDateTime> checkedTime = new HashMap<>();   // 알림 확인 시간
    private Set<String> checked = new HashSet<>();                      // 알림을 확인했는지 안했는지
}
