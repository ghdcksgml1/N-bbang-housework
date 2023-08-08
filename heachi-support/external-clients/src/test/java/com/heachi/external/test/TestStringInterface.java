package com.heachi.external.test;

import com.heachi.external.annotation.ExternalClients;
import com.heachi.external.test.dto.TestResponse;
import org.springframework.web.service.annotation.GetExchange;

@ExternalClients(baseUrl = "https://open.er-api.com")
public interface TestStringInterface {

    @GetExchange("/v6/latest")
    public TestResponse getLatest();
}
