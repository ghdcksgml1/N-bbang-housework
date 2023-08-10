package com.heachi.external.clients.oauth2.kakao;

import com.heachi.external.annotation.ExternalClients;
import com.heachi.external.clients.oauth2.kakao.response.KakaoProfileResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Map;

@ExternalClients(baseUrl = "oauth2.client.kakao.profile-uri")
public interface KakaoProfileClients {

    @GetExchange
    public KakaoProfileResponse getProfile(@RequestHeader(value = "Authorization") String headers);

}
