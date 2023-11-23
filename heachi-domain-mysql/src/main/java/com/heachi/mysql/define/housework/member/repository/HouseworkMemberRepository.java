package com.heachi.mysql.define.housework.member.repository;

import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.info.repository.HouseworkInfoRepositoryCustom;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface HouseworkMemberRepository extends JpaRepository<HouseworkMember, Long>, HouseworkMemberRepositoryCustom {

    // houseworkInfo와 일치하는 값들을 삭제한다.
    @Transactional
    public void deleteByHouseworkInfo(HouseworkInfo houseworkInfo);

}
