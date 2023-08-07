package com.heachi.mongo.define.notify.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotifyType {
    NOTE ("공지");

    private final String text;
}
