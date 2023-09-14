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

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotifyRepositoryImpl implements NotifyRepositoryCustom {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<Notify> findNotifyByReceiveUserIdsPaging(String userIds, int page) {
        return Mono.just(PageRequest.of(page, 10, Sort.by("createdTime").descending()))
                .map(pageable -> {
                            Query query = new Query()
                                    .with(pageable);
                            query.addCriteria(Criteria.where("receiveUserIds").in(List.of(userIds)));

                            return query;
                        }
                ).flatMapMany(query -> mongoTemplate.find(query, Notify.class, "notify"));
    }
}
