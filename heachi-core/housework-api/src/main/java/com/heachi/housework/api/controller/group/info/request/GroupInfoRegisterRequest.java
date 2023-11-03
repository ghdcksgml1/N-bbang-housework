package com.heachi.housework.api.controller.group.info.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
public class GroupInfoRegisterRequest {
    private Long groupMemberId;
    private Long groupId;
    @NotEmpty
    private boolean status;

    @Builder
    private GroupInfoRegisterRequest(Long groupMemberId, Long groupId, boolean status) {
        this.groupMemberId = groupMemberId;
        this.groupId = groupId;
        this.status = status;
    }
}
