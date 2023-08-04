package com.heachi.mysql.define.entity.repository;

import com.heachi.mysql.define.hello.Hello;
import com.heachi.mysql.define.hello.repository.HelloRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@SpringBootTest
class HelloRepositoryTest {

    @Autowired
    private HelloRepository helloRepository;

    @AfterEach
    void tearDown() {
        helloRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("정의한 JpaRepository 테스트")
    void helloRepositoryTest() {
        // given
        Hello hello = Hello.builder()
                .hello("안녕하세요")
                .build();
        helloRepository.save(hello);

        // when
        Hello findHello = helloRepository.findHelloOrderByDescOne();

        // then
        Assertions.assertThat(findHello.getHello())
                .isEqualTo("안녕하세요");
    }

}