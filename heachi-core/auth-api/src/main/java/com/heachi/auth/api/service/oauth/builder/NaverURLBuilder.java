package com.heachi.auth.api.service.oauth.builder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NaverURLBuilder implements OAuthURLBuilder {

    @Value("${oauth2.client.naver.authorization-uri}") private String authorizationUri;
    @Value("${oauth2.client.naver.token-uri}") private String tokenUri;
    @Value("${oauth2.client.naver.profile-uri}") private String profileUri;

    @Value("${oauth2.client.naver.client-id}") private String clientId;
    @Value("${oauth2.client.naver.redirect-uri}") private String redirectUri;
    @Value("${oauth2.client.naver.client-secret}") private String clientSecret;

    // https://nid.naver.com/oauth2.0/authorize
    @Override
    public String authorize(String state) {
        return authorizationUri
                + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&state=" + state;
    }

    // https://nid.naver.com/oauth2.0/token
    @Override
    public String token(String code, String state) {
        return tokenUri
                + "?grant_type=authorization_code"
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&code=" + code
                + "&state=" + state;
    }

    @Override
    public String profile() {
        return profileUri;
    }
}
