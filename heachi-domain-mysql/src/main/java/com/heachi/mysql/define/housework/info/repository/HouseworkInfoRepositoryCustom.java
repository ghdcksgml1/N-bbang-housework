package com.heachi.mysql.define.housework.info.repository;

import com.heachi.mysql.define.housework.info.HouseworkInfo;

import java.util.List;
import java.util.Optional;

public interface HouseworkInfoRepositoryCustom {

    // HouseworkInfo 조회하는데 fetch Join으로 가져옴. HouseworkTodo.makeTodoReferInfo에서 사용
    public Optional<HouseworkInfo> findHouseworkInfoByIdJoinFetchHouseworkMembers(Long houseworkInfoId);

    // HouseworkInfo 조회하는데 GroupInfoId로 조회한다.
    public List<HouseworkInfo> findHouseworkInfoByGroupInfoId(Long groupId);

    // HouseworkInfo 리스트를 받아 삭제
    public void deleteHouseworkInfoByHouseworkInfoList(List<HouseworkInfo> houseworkInfoList);

}
