package com.heachi.housework.api.service.group.info.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupInfoCreateServiceRequest {

    private String bgColor;     // 그룹 배경색
    private String colorCode;   // 그룹 색상코드
    private String gradient;    // 그룹의 css 속성
    private String name;        // 그룹 이름
    private String info;        // 그룹 소개

    private String email;       // 유저 이메일

    @Builder
    private GroupInfoCreateServiceRequest(String bgColor, String colorCode, String gradient,
                                          String name, String info, String email) {
        this.bgColor = bgColor;
        this.colorCode = colorCode;
        this.gradient = gradient;
        this.name = name;
        this.info = info;
        this.email = email;
    }
}
