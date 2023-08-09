package com.heachi.external.clients.oauth2.kakao;

import com.heachi.external.annotation.ExternalClients;
import com.heachi.external.clients.oauth2.kakao.response.KakaoTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.web.service.annotation.PostExchange;

import java.net.URI;


@ExternalClients(baseUrl = "ouath2.client.kakao.token-uri")
public interface KakaoTokenClients {

    @PostExchange(contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public KakaoTokenResponse getToken(URI uri);

}
