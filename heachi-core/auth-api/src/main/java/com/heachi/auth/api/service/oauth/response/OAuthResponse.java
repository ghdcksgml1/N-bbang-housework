package com.heachi.auth.api.service.oauth.response;

import com.heachi.mysql.define.user.constant.UserPlatformType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class OAuthResponse {
    private String platformId;
    private UserPlatformType platformType;
    private String email;
    private String name;
    private String profileImageUrl;

    @Builder
    private OAuthResponse(String platformId, UserPlatformType platformType, String email, String name, String profileImageUrl) {
        this.platformId = platformId;
        this.platformType = platformType;
        this.email = email;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }
}
