package com.heachi.admin.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
    // JwtException
    JWT_TOKEN_EXPIRED("JWT 토큰이 만료되었습니다."),
    JWT_UNSUPPORTED("지원하지 않는 JWT 토큰입니다."),
    JWT_MALFORMED("올바른 JWT 토큰의 형태가 아닙니다."),
    JWT_SIGNATURE("올바른 SIGNATURE가 아닙니다."),
    JWT_ILLEGAL_ARGUMENT("잘못된 정보를 넣었습니다."),
    JWT_USER_NOT_FOUND("사용자를 찾을 수 없습니다."),

    // RefreshTokenException
    JWT_NOT_EXIST_RTK("해당 Refresh Token이 존재하지 않습니다."),
    JWT_INVALID_RTK("유효하지 않은 Refresh Token입니다."),

    // OAuthException
    OAUTH_INVALID_STATE("STATE 검증에 실패하였습니다."),
    OAUTH_INVALID_TOKEN_URL("tokenURL이 정확하지 않습니다."),
    OAUTH_INVALID_ACCESS_TOKEN("잘못된 access_token 입니다."),

    // LoginState
    LOGINSTATE_IS_NOT_USE("해당 LoginState를 사용할 수 없습니다."),
    LOGINSTATE_INVALID_VALUE("LoginState 정보가 잘못되었습니다."),
    LOGINSTATE_NOT_FOUND("LoginState를 찾을 수 없습니다."),

    // AuthException
    AUTH_INVALID_REGISTER("잘못된 회원가입 요청입니다."),
    AUTH_DUPLICATE_UNAUTH_REGISTER("중복된 회원가입 요청입니다."),
    AUTH_SERVER_NOT_RESPOND("인증 서버가 응답하지 않습니다."),
    AUTH_UNAUTHORIZED("현재 권한으로 실행할 수 없는 요청입니다."),

    // NotifyException
    NOTIFY_NOT_FOUND("해당 아이디의 알림을 찾을 수 없습니다."),
    NOTIFY_DUPLICATE_ID("SendUser와 ReceiveUser가 같습니다."),
    ;

    private final String text;
}
