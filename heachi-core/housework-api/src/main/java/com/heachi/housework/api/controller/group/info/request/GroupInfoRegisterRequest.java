package com.heachi.housework.api.controller.group.info.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfoRegisterRequest {
    private Long groupMemberId;
    private Long groupId;
    @NotEmpty
    private boolean status;
}
