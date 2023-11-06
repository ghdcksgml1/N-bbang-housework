package com.heachi.mysql.define.group.member.repository;

import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long>, GroupMemberRepositoryCustom {
    // User 정보로 GroupMember 조회
    Optional<GroupMember> findByUser(User user);

    Optional<GroupMember> findByUserAndGroupInfo(User user, GroupInfo groupInfo);

    Optional<GroupMember> findByUserAndGroupInfo_id(User user, Long groupId);

    @Query("select gm from GROUP_MEMBER gm where gm.id = :id and gm.groupInfo.id = :groupId")
    Optional<GroupMember> findByIdAndGroupInfoId(@Param("id")Long groupMemberId, @Param("groupId")Long groupId);
}
