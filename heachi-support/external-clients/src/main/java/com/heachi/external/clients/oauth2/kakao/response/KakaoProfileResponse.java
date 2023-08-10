package com.heachi.external.clients.oauth2.kakao.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@NoArgsConstructor
@ToString
public class KakaoProfileResponse {
    private Long id;
    private KakaoAccount kakao_account;


    public KakaoProfileResponse(Long id, KakaoAccount kakao_account) {
        this.id = id;
        this.kakao_account = kakao_account;
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class KakaoAccount {
        private String email;
        private KakaoProfile profile;

        public KakaoAccount(String email, KakaoProfile profile) {
            this.email = email;
            this.profile = profile;
        }

        @Getter
        @NoArgsConstructor
        @ToString
        public static class KakaoProfile {
            private String nickname;
            private String thumbnail_image_url;

            public KakaoProfile(String nickname, String thumbnail_image_url) {
                this.nickname = nickname;
                this.thumbnail_image_url = thumbnail_image_url;
            }
        }
    }
}