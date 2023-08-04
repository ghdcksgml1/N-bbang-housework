package com.heachi.admin.common.exception.jwt;

public class JwtExceptionMsg {
    public static final String JWT_TOKEN_EXPIRED = "JWT 토큰이 만료되었습니다.";
    public static final String JWT_UNSUPPORTED = "지원하지 않는 JWT 토큰입니다.";
    public static final String JWT_MALFORMED = "올바른 JWT 토큰의 형태가 아닙니다.";
    public static final String JWT_SIGNATURE = "올바른 SIGNATURE가 아닙니다.";
    public static final String JWT_ILLEGAL_ARGUMENT = "잘못된 정보를 넣었습니다.";
}
