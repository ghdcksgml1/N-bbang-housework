package com.heachi.mysql.define.group.info.repository.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class GroupInfoUserGroupResponse {
    private Long id;                                    // 그룹 아이디
    private String name;                                // 그룹 이름
    private List<GroupInfoGroupMember> groupMembers;    // 그룹 멤버

    @Builder
    private GroupInfoUserGroupResponse(Long id, String name, List<GroupInfoGroupMember> groupMembers) {
        this.id = id;
        this.name = name;
        this.groupMembers = groupMembers;
    }
}
