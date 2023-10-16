package com.heachi.mysql.define.group.member.repository;

import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long>, GroupMemberRepositoryCustom {
}
