package com.heachi.auth.api.service.auth.request;


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
    private String platformId;
    private UserRole role;
    private String name;
    private String email;
    private String phoneNumber;
    private String profileImageUrl;
}
