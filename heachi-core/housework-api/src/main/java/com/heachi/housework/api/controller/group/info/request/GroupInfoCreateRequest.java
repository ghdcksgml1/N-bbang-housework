package com.heachi.housework.api.controller.group.info.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@ToString
@NoArgsConstructor
public class GroupInfoCreateRequest {
    @NotEmpty
    private String bgColor;     // 그룹 배경색

    @NotEmpty
    private String colorCode;   // 그룹 색상코드

    @NotEmpty
    private String gradient;    // 그룹의 css 속성

    @NotEmpty
    private String name;        // 그룹 이름

    @Length(max = 512)
    private String info;        // 그룹 소개
}
