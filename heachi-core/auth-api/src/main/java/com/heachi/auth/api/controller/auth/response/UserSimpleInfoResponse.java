package com.heachi.auth.api.controller.auth.response;

import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSimpleInfoResponse {
    private UserRole role;
    private String name;
    private String email;
    private String profileImageUrl;

    @Builder
    private UserSimpleInfoResponse(UserRole role, String name, String email, String profileImageUrl) {
        this.role = role;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public static UserSimpleInfoResponse of(User user) {
        return UserSimpleInfoResponse.builder()
                .role(user.getRole())
                .name(user.getName())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
