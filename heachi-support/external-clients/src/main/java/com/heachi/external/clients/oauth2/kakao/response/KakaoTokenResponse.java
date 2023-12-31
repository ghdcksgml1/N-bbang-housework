package com.heachi.external.clients.oauth2.kakao.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class KakaoTokenResponse {
    private String access_token;

    public KakaoTokenResponse(String access_token) {
        this.access_token = access_token;
    }
}
