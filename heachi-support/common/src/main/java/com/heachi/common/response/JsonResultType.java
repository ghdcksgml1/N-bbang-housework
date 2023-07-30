package com.heachi.common.response;

import com.heachi.common.heachi.HeachiEnum;
import lombok.Getter;

@Getter
public enum JsonResultType implements HeachiEnum {
    SUCCESS(200, "성공"),
    FAIL(500, "실패");

    private Integer code;
    private String title;

    JsonResultType(Integer code, String title) {
        this.code = code;
        this.title = title;
    }
}
