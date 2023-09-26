package com.heachi.auth.api.service.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtTokenDTO {
    private String accessToken;
    private String refreshToken;

    @Builder
    public JwtTokenDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
