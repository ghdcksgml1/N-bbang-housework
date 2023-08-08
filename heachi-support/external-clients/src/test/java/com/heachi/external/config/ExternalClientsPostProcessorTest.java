package com.heachi.external.config;

import com.heachi.external.TestConfig;
import com.heachi.external.test.TestStringInterface;
import com.heachi.external.test.TestYmlInterface;
import com.heachi.external.test.dto.TestResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ExternalClientsPostProcessorTest extends TestConfig {

    @Autowired
    private TestYmlInterface testYmlInterface;

    @Autowired
    private TestStringInterface testStringInterface;

    @Test
    @DisplayName("@ExternalClients의 baseUrl에 yml 파일의 값을 가져온다.")
    void externalClientsAnnotationBaseUrlYmlTest() {
        // given

        // when
        TestResponse result = testYmlInterface.getLatest();

        // then
        assertThat(result.getResult()).isEqualTo("success");
        assertThat(result.getRates().get("USD")).isEqualTo(1.0);
    }

    @Test
    @DisplayName("@ExternalClients의 baseUrl의 값으로 yml 파일에서 가져올 수 없다면, baseUrl에 String을 등록한다.")
    void externalClientsAnnotationBaseUrlStringTest() {
        // given

        // when
        TestResponse result = testStringInterface.getLatest();

        // then
        assertThat(result.getResult()).isEqualTo("success");
        assertThat(result.getRates().get("USD")).isEqualTo(1.0);
    }
}