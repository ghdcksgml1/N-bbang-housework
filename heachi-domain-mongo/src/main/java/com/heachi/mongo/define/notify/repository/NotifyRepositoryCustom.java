package com.heachi.mongo.define.notify.repository;

import com.heachi.mongo.define.notify.Notify;
import reactor.core.publisher.Flux;

public interface NotifyRepositoryCustom {

    public Flux<Notify> findNotifyByReceiveUserIdsPaging(String userIds, int page);

}
