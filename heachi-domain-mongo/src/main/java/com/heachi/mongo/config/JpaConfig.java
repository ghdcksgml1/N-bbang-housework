package com.heachi.mongo.config;

import com.heachi.mongo.define.notify.Notify;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import reactor.core.publisher.Mono;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.heachi.mongo.define")
@EnableReactiveMongoRepositories(basePackages = {"com.heachi.mongo.define"})
@RequiredArgsConstructor
public class JpaConfig {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    // notify Collection Setting
    @Bean
    public void mongoDBinit() {
        // Collection 초기 세팅을 위해 Notify 객체를 생성했다가 지움
        reactiveMongoTemplate.insert(Notify.builder().build())
                .flatMap(notify -> reactiveMongoTemplate.remove(notify)
                        .then(reactiveMongoTemplate.executeCommand("{ convertToCapped: 'notify', size: 8192 }"))
                        .then(reactiveMongoTemplate.executeCommand("{ collStats: 'notify' }"))
                        .doOnNext(stats -> System.out.println("stats = " + stats))
                )
                .subscribe();
    }
}