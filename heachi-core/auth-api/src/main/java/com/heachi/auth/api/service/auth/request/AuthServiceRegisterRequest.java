package com.heachi.auth.api.service.auth.request;


import com.heachi.auth.api.controller.auth.request.AuthRegisterRequest;
import com.heachi.mysql.define.user.constant.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthServiceRegisterRequest {
    private String email;
    private UserRole role;
    private String phoneNumber;

    public static AuthServiceRegisterRequest of(AuthRegisterRequest request) {
        return AuthServiceRegisterRequest.builder()
                .email(request.getEmail())
                .role(request.getRole())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }
}
