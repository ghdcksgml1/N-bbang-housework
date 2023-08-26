package com.heachi.auth.api.service.auth.response;

import com.heachi.mysql.define.user.constant.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthServiceLoginResponse {
    private String token;
    private UserRole role;

    @Builder
    private AuthServiceLoginResponse(String token, UserRole role) {
        this.token = token;
        this.role = role;
    }
}
