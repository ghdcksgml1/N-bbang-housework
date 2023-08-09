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

    @Getter
    @NoArgsConstructor
    @ToString
    public class KakaoAccount {
        private String email;
        private KakaoProfile profile;

        @Getter
        @NoArgsConstructor
        @ToString
        public class KakaoProfile {
            private String nickname;
            private String thumbnail_image_url;
        }
    }
}