package com.heachi.auth.api.service.oauth.builder;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoURLBuilder implements OAuthURLBuilder {

    @Value("${oauth2.client.kakao.authorization-uri}") private String authorizationUri;
    @Value("${oauth2.client.kakao.token-uri}") private String tokenUri;
    @Value("${oauth2.client.kakao.profile-uri}") private String profileUri;

    @Value("${oauth2.client.kakao.client-id}") private String clientId;
    @Value("${oauth2.client.kakao.redirect-uri}") private String redirectUri;
    @Value("${oauth2.client.kakao.client-secret}") private String clientSecret;

    // "https://kauth.kakao.com/oauth/authorize"
    @Override
    public String authorize(String state) {
        return authorizationUri
                + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&state=" + state
                + "&scope=openid";
    }

    // "https://kauth.kakao.com/oauth/token"
    @Override
    public String token(String code, String state) {
        return tokenUri
                + "?grant_type=authorization_code"
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&redirect_uri=" + redirectUri
                + "&code=" + code;
    }

    //
    @Override
    public String profile() {
        return profileUri;
    }
}
