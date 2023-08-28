package com.heachi.external.clients.auth.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class UserInfoResponse {
    private String platformId;
    private String platformType;
    private String role;
    private String name;

    public UserInfoResponse(String platformId, String platformType, String role, String name) {
        this.platformId = platformId;
        this.platformType = platformType;
        this.role = role;
        this.name = name;
    }
}
