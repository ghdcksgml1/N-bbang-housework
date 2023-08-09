package com.heachi.auth.api.service.oauth.builder;

public interface OAuthURLBuilder {

    // 로그인 인증 요청
    String authorize(String state);

    // 토큰 발급 요청
    String token(String code);

    // 프로필 요청
    String profile();
}
