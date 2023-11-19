package com.heachi.mysql.define.housework.member.repository;

import com.heachi.mysql.define.housework.info.HouseworkInfo;

import java.util.List;

public interface HouseworkMemberRepositoryCustom {
    // hosueworkInfo의 담당자 리스트를 뽑은 후 groupMemberIdList와 동일하지 않다면 false 리턴
    public boolean isSameHouseworkMemberIdAndGroupMemberId(HouseworkInfo info, List<Long> groupMemberIdList);
}
