package com.heachi.external.clients.oauth2.naver;

import com.heachi.external.annotation.ExternalClients;
import com.heachi.external.clients.oauth2.naver.response.NaverProfileResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;

@ExternalClients(baseUrl = "oauth2.client.naver.profile-uri")
public interface NaverProfileClients {

    @GetExchange
    public NaverProfileResponse getProfile(@RequestHeader(value = "Authorization") String headers);
}
