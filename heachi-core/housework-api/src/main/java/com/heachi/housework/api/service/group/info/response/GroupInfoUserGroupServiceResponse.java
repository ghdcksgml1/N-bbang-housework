package com.heachi.housework.api.service.group.info.response;

import com.heachi.mysql.define.group.info.repository.response.GroupInfoGroupMember;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class GroupInfoUserGroupServiceResponse {
    private Long id;                                    // 그룹 아이디
    private String name;                                // 그룹 이름
    private List<GroupInfoGroupMember> groupMembers;    // 그룹 멤버
    private int remainTodoListCnt;                      // 남은 집안일 개수
    private int progressPercent;                        // 진행률 (소수점 첫째자리에서 반올림해서 정수로 표현)

    @Builder
    private GroupInfoUserGroupServiceResponse(Long id, String name, List<GroupInfoGroupMember> groupMembers,
                                       int remainTodoListCnt, int progressPercent) {
        this.id = id;
        this.name = name;
        this.groupMembers = groupMembers;
        this.remainTodoListCnt = remainTodoListCnt;
        this.progressPercent = progressPercent;
    }
}
