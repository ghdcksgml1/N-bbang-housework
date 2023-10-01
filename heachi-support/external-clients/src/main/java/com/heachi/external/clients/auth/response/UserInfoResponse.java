package com.heachi.external.clients.auth.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class UserInfoResponse {
    private String role;
    private String name;
    private String email;
    private String profileImageUrl;

    @Builder
    public UserInfoResponse(String role, String name, String email, String profileImageUrl) {
        this.role = role;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }
}
