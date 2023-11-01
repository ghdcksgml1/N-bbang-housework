package com.heachi.mysql.define.group.member.repository;

import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long>, GroupMemberRepositoryCustom {
    // User 정보로 GroupMember 조회
    Optional<GroupMember> findByUser(User user);

    Optional<GroupMember> findByUserAndGroupInfo(User user, GroupInfo groupInfo);
}
