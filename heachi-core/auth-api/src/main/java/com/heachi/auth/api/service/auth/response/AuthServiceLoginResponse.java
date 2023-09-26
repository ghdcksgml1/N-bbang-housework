package com.heachi.auth.api.service.auth.response;

import com.heachi.mysql.define.user.constant.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthServiceLoginResponse {
    private String accessToken;
    private String refreshToken;
    private UserRole role;

    @Builder
    public AuthServiceLoginResponse(String accessToken, String refreshToken, UserRole role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
    }
}
