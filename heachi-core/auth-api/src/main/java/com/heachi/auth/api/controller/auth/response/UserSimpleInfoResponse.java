package com.heachi.auth.api.controller.auth.response;

import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import com.heachi.mysql.define.user.constant.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSimpleInfoResponse {
    private Long id;
    private String platformId;
    private UserPlatformType platformType;
    private UserRole role;
    private String name;
    private String email;
    private String profileImageUrl;

    @Builder
    private UserSimpleInfoResponse(Long id, String platformId, UserPlatformType platformType, UserRole role, String name, String email, String profileImageUrl) {
        this.id = id;
        this.platformId = platformId;
        this.platformType = platformType;
        this.role = role;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public static UserSimpleInfoResponse of(User user) {
        return UserSimpleInfoResponse.builder()
                .id(user.getId())
                .platformId(user.getPlatformId())
                .platformType(user.getPlatformType())
                .role(user.getRole())
                .name(user.getName())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
