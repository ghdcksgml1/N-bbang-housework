package com.heachi.external.clients.oauth2.naver;

import com.heachi.external.annotation.ExternalClients;
import com.heachi.external.clients.oauth2.naver.response.NaverTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.web.service.annotation.PostExchange;

import java.net.URI;

@ExternalClients(baseUrl = "ouath2.client.naver.token-uri")
public interface NaverTokenClients {

    @PostExchange(contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public NaverTokenResponse getToken(URI uri);
}
