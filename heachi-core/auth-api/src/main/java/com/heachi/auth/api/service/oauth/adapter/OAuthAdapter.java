package com.heachi.auth.api.service.oauth.adapter;

import com.heachi.auth.api.service.oauth.response.OAuthResponse;

/**
 * Adapter 패턴을 이용한 OAuth 2.0 로그인
 *
 * OAuth2.0의 로그인 과정 *
 *
 * 1. 로그인 페이지 접속
 * 2. 로그인 후 리다이렉트
 * 3. 토큰 요청
 * 4. 얻은 토큰으로 사용자 정보 가져옴
 *
 * 1,2는 프론트 3,4는 백엔드 담당
 */
public interface OAuthAdapter {

    String getToken(String tokenURL);

    OAuthResponse getProfile(String accessToken);

}
