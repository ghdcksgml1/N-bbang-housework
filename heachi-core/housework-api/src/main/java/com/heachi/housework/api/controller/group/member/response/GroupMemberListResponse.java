package com.heachi.housework.api.controller.group.member.response;

import com.heachi.mysql.define.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class GroupMemberListResponse {
    private Long groupMemberId;
    private String username;

    @Builder
    public GroupMemberListResponse(Long groupMemberId, String username) {
        this.groupMemberId = groupMemberId;
        this.username = username;
    }
}
