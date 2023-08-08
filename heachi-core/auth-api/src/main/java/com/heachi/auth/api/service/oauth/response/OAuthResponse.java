package com.heachi.auth.api.service.oauth.response;

import com.heachi.mysql.define.user.constant.UserPlatformType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthResponse {
    private String platformId;
    private String email;
    private String name;
    private String profileImageUrl;

    @Builder
    private OAuthResponse(String platformId, String email, String name, String profileImageUrl) {
        this.platformId = platformId;
        this.email = email;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }
}
