package com.heachi.mysql.define.group.info.repository;

import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.response.GroupInfoUserGroupResponse;

import java.util.List;

public interface GroupInfoRepositoryCustom {

    // 유저 이메일을 통해 해당 유저가 속한 그룹의 정보들을 가져와 주는 쿼리
    public List<GroupInfoUserGroupResponse> findGroupInfoUserGroupResponseListByUserEmail(String email);
}
