package com.heachi.mysql.define.housework.member.repository;

import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HouseworkMemberRepository extends JpaRepository<HouseworkMember, Long>, HouseworkMemberRepositoryCustom {

    // houseworkInfo와 일치하는 값들을 삭제한다.
    public void deleteByHouseworkInfo(HouseworkInfo houseworkInfo);

    // houseworkInfo를 외래키로 가진 HouseworkMember 리스트 조회
    public List<HouseworkMember> findByHouseworkInfo(HouseworkInfo houseworkInfo);
}
