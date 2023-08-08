package com.heachi.mongo.config;

import com.heachi.mongo.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JpaConfigTest extends TestConfig {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Test
    @DisplayName("Spring Applicatoin이 띄워질때, MongoDB capped 설정이 true가 된다.")
    void mongoDBConfigurationInitialized() {
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