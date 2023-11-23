package com.heachi.mysql.define.group.info.repository;

import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.response.GroupInfoUserGroupResponse;

import java.util.List;
import java.util.Optional;

public interface GroupInfoRepositoryCustom {

    // 유저 이메일을 통해 해당 유저가 속한 그룹의 정보들을 가져와 주는 쿼리
    public List<GroupInfoUserGroupResponse> findGroupInfoUserGroupResponseListByUserEmail(String email);

    // 그룹 정보를 조회해오면서 fetch join으로 User 정보까지 조회한다.
    public Optional<GroupInfo> findGroupInfoByGroupIdJoinFetchUser(Long groupId);
}
