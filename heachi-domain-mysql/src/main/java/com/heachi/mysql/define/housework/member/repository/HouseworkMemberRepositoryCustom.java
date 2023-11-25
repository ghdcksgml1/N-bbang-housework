package com.heachi.mysql.define.housework.member.repository;

import com.heachi.mysql.define.housework.info.HouseworkInfo;

import java.util.List;

public interface HouseworkMemberRepositoryCustom {
    // HouseworkInfoList를 받아서 해당 HouseworkInfo를 외래키로 가진 HouseworkMember를 삭제하는 쿼리
    public void deleteByHouseworkInfoList(List<HouseworkInfo> houseworkInfoList);
  
    // hosueworkInfo의 담당자 리스트를 뽑은 후 groupMemberIdList와 동일하지 않다면 false 리턴
    public boolean isSameHouseworkMemberIdAndGroupMemberId(HouseworkInfo info, List<Long> groupMemberIdList);
}
