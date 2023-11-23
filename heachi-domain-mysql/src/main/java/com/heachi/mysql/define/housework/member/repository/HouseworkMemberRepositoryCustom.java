package com.heachi.mysql.define.housework.member.repository;

import com.heachi.mysql.define.housework.info.HouseworkInfo;

import java.util.List;

public interface HouseworkMemberRepositoryCustom {
    // HouseworkInfoList를 받아서 해당 HouseworkInfo를 외래키로 가진 HouseworkMember를 삭제하는 쿼리
    public void deleteByHouseworkInfoList(List<HouseworkInfo> houseworkInfoList);
}
