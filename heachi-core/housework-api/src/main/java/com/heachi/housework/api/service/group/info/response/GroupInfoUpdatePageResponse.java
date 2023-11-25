package com.heachi.housework.api.service.group.info.response;

import com.heachi.mysql.define.group.info.GroupInfo;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class GroupInfoUpdatePageResponse {
    private String bgColor;
    private String colorCode;
    private String gradient;
    private String name;
    private String info;

    @Builder
    public GroupInfoUpdatePageResponse(String bgColor, String colorCode, String gradient, String name, String info) {
        this.bgColor = bgColor;
        this.colorCode = colorCode;
        this.gradient = gradient;
        this.name = name;
        this.info = info;
    }

    public static GroupInfoUpdatePageResponse of(GroupInfo groupInfo) {
        return GroupInfoUpdatePageResponse.builder()
                .bgColor(groupInfo.getBgColor())
                .colorCode(groupInfo.getColorCode())
                .gradient(groupInfo.getGradient())
                .name(groupInfo.getName())
                .info(groupInfo.getInfo())
                .build();

    }

}
