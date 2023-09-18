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

}