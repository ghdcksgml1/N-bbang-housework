package com.heachi.mysql.define.group.info.repository;

import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupInfoRepository extends JpaRepository<GroupInfo, Long>, GroupInfoRepositoryCustom {
    // 그룹장 User 정보로 조회
    Optional<GroupInfo> findByUser(User user);

    Optional<GroupInfo> findByJoinCode(String joinCode);
}
