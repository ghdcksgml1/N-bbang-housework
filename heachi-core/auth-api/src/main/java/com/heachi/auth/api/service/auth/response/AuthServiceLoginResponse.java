package com.heachi.auth.api.service.auth.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthServiceLoginResponse {
    private String token;

    @Builder
    private AuthServiceLoginResponse(String token) {
        this.token = token;
    }
}
