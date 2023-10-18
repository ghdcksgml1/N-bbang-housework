package com.heachi.mysql.define.group.member.repository;

import com.heachi.mysql.define.group.member.GroupMember;

import java.util.List;

public interface GroupMemberRepositoryCustom {

    // Email과 GroupId를 통해 사용자가 해당 그룹의 구성원인지 확인하는 쿼리
    public boolean existsGroupMemberByUserEmailAndGroupInfoId(String email, Long groupId);

    // GroupId를 통해 그룹의 구성원을 리턴해주는 쿼리
    public List<GroupMember> findGroupMemberByGroupId(Long groupId);
}
