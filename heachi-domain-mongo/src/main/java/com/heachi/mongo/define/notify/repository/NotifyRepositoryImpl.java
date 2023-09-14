package com.heachi.mongo.define.notify.repository;

import com.heachi.mongo.define.notify.Notify;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class NotifyRepositoryImpl implements NotifyRepositoryCustom {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<Notify> findNotifyByReceiveUserIdsPaging(String userId, int page) {
        return Mono.just(PageRequest.of(page, 10, Sort.by("createdTime").descending()))
                .map(pageable -> {
                            Query query = new Query()
                                    .with(pageable);
                            query.addCriteria(Criteria.where("receiveUserIds").in(userId));

                            return query;
                        }
                ).flatMapMany(query -> mongoTemplate.find(query, Notify.class, "notify"));
    }

    @Override
    public Mono<Notify> findNotifyByIdWhereReceiveUserIdsIn(String userId, String notifyId) {
        return Mono.just(new Query().addCriteria(Criteria.where("id").is(notifyId)
                .and("receiveUserIds").in(userId)))
                .flatMap(query -> mongoTemplate.findOne(query, Notify.class, "notify"));
    }


}
