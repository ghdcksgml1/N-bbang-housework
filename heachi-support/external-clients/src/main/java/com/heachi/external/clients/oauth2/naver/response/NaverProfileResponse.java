package com.heachi.external.clients.oauth2.naver.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor
@ToString
public class NaverProfileResponse {
    private NaverAccount response;

    @Getter
    @NoArgsConstructor
    @ToString
    public class NaverAccount {
        private String id;
        private String email;
        private String nickname;
        private String profile_image;
    }
}
