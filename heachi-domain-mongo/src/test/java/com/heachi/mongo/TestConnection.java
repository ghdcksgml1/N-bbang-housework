package com.heachi.mongo;

import com.mongodb.reactivestreams.client.MongoClient;
import org.bson.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestConnection extends TestConfig {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Test
    @DisplayName("MongoDB에 컬렉션을 고정된 크기로 고정시키기 위해 capped 설정을 해준다.")
    void mongoDBcappedConfiguration() {
        // given
        reactiveMongoTemplate.executeCommand("{ convertToCapped: 'notify', size: 8192 }");

        // when
        reactiveMongoTemplate.executeCommand("{ collStats: 'notify' }")
                // then
                .as(StepVerifier::create)
                .expectNextMatches(document -> {
                    assertThat(document.get("capped")).isEqualTo(true);
                    assertThat(document.get("totalSize")).isEqualTo(8192);

                    return true;
                })
                .verifyComplete();
    }
}
