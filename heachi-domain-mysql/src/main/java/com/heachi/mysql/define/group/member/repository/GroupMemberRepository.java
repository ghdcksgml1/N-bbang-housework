package com.heachi.mysql.define.group.member.repository;

import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    // User로 GroupMember 검색
    Optional<GroupMember> findByUser(User user);

    // UserId로 GroupMember 검색
    Optional<GroupMember> findByUserId(Long userId);

    @Query("SELECT gm FROM GROUP_MEMBER gm WHERE gm.user = :user AND gm.groupInfo.id = :groupInfoId")
    Optional<GroupMember> findByUserEmailAndGroupInfoId(@Param("email") String email, @Param("groupInfoId") Long groupInfoId);
}
