package com.heachi.mongo.define.notify.repository;

import com.heachi.mongo.define.notify.Notify;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface NotifyRepository extends ReactiveMongoRepository<Notify, String> {

    @Tailable
    @Query("{ receiveUserIds : ?0 }")
    public Flux<Notify> findByReceiveUserIds(String userIds);
}
