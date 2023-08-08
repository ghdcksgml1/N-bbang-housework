package com.heachi.external.test;

import com.heachi.external.annotation.ExternalClients;
import com.heachi.external.test.dto.TestResponse;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Map;

@ExternalClients(baseUrl = "test.url")
public interface TestYmlInterface {

    @GetExchange("/v6/latest")
    public TestResponse getLatest();
}
