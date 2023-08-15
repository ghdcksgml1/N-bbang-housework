package com.heachi.external.clients.oauth2.naver.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor
@ToString
public class NaverProfileResponse {
    private NaverAccount response;

    public NaverProfileResponse(NaverAccount response) {
        this.response = response;
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class NaverAccount {

        public NaverAccount(String id, String email, String nickname, String profile_image) {
            this.id = id;
            this.email = email;
            this.nickname = nickname;
            this.profile_image = profile_image;
        }

        private String id;
        private String email;
        private String nickname;
        private String profile_image;
    }
}
