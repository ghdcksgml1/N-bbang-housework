package com.heachi.mysql.define.housework.member.repository;

import com.heachi.mysql.define.housework.member.HouseworkMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseworkMemberRepository extends JpaRepository<HouseworkMember, Long> {
}
