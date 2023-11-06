package com.heachi.housework.api.controller.group.member.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupMemberResponse {
    private Long groupMemberId;
    private String username;

    @Builder
    public GroupMemberResponse(Long groupMemberId, String username) {
        this.groupMemberId = groupMemberId;
        this.username = username;
    }
}
