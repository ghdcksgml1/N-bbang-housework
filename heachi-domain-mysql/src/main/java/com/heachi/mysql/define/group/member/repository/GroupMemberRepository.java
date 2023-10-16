package com.heachi.mysql.define.group.member.repository;

import com.heachi.mysql.define.group.member.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long>, GroupMemberRepositoryCustom {
}
