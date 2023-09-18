package com.heachi.mongo.define.notify.repository;

import com.heachi.mongo.define.notify.Notify;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotifyRepositoryCustom {

    /**
     * userId가 받는 알림을 페이징한다. (ReceiveUserIds In userId)
     */
    public Flux<Notify> findNotifyByReceiveUserIdsPaging(String userId, int page);

    /**
     * notifyId로 notify를 검색하는데, userId가 recevierUserIds에 존재하는지
     */
    public Mono<Notify> findNotifyByIdWhereReceiveUserIdsIn(String userId, String notifyId);

}
