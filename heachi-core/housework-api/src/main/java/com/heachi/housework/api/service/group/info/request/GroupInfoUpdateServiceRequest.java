package com.heachi.housework.api.service.group.info.request;

import com.heachi.housework.api.controller.group.info.request.GroupInfoCreateRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupInfoUpdateServiceRequest {
    private Long groupId;
    private String bgColor;
    private String colorCode;
    private String gradient;
    private String name;
    private String info;

    @Builder
    public GroupInfoUpdateServiceRequest(Long groupId, String bgColor, String colorCode, String gradient, String name, String info) {
        this.groupId = groupId;
        this.bgColor = bgColor;
        this.colorCode = colorCode;
        this.gradient = gradient;
        this.name = name;
        this.info = info;
    }

    public static GroupInfoUpdateServiceRequest of(GroupInfoCreateRequest request, Long groupId) {
        return GroupInfoUpdateServiceRequest.builder()
                .groupId(groupId)
                .bgColor(request.getBgColor())
                .colorCode(request.getColorCode())
                .gradient(request.getGradient())
                .name(request.getName())
                .info(request.getInfo())
                .build();
    }
}
