package com.heachi.external.clients.oauth2.naver.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class NaverTokenResponse {
    private String access_token;

    public NaverTokenResponse(String access_token) {
        this.access_token = access_token;
    }
}
