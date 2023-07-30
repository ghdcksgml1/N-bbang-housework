package com.heachi.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JsonResult<T> {
    private Integer resCode;
    private T resObj;
    private String resMsg;

    @Builder
    public JsonResult(Integer resCode, T resObj, String resMsg) {
        this.resCode = resCode;
        this.resObj = resObj;
        this.resMsg = resMsg;
    }

    public static JsonResult successOf() {
        return JsonResult.builder()
                .resCode(JsonResultType.SUCCESS.getCode())
                .resMsg(JsonResultType.SUCCESS.getTitle())
                .build();
    }

    public static <T> JsonResult successOf(T resObj) {
        return JsonResult.builder()
                .resCode(JsonResultType.SUCCESS.getCode())
                .resObj(resObj)
                .resMsg(JsonResultType.SUCCESS.getTitle())
                .build();
    }

    public static JsonResult failOf() {
        return JsonResult.builder()
                .resCode(JsonResultType.FAIL.getCode())
                .resMsg(JsonResultType.FAIL.getTitle())
                .build();
    }

    public static <T> JsonResult failOf(String resMsg) {
        return JsonResult.builder()
                .resCode(JsonResultType.FAIL.getCode())
                .resMsg(resMsg)
                .build();
    }
}
