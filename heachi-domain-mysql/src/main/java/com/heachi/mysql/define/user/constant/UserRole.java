package com.heachi.mysql.define.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    UNAUTH("미인증"),
    USER("유저"),
    WITHDRAW("탈퇴");

    private final String text;
}
