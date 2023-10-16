package com.heachi.mysql.define.group.member.repository;

import com.heachi.mysql.TestConfig;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.GroupInfoRepository;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.group.member.constant.GroupMemberRole;
import com.heachi.mysql.define.group.member.constant.GroupMemberStatus;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.mysql.define.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GroupMemberRepositoryTest extends TestConfig {

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupInfoRepository groupInfoRepository;

    @AfterEach
    void tearDown() {
        groupMemberRepository.deleteAllInBatch();
        groupInfoRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("email과 groupId를 통해 사용자의 그룹멤버 여부를 판단할 수 있다.")
    void existsGroupMemberByUserEmailAndGroupInfoId() {
        // given
        User user = userRepository.save(generateUser());
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(user));
        GroupMember groupMember = groupMemberRepository.save(generateGroupMember(user, groupInfo));

        // when
        boolean result = groupMemberRepository.existsGroupMemberByUserEmailAndGroupInfoId(user.getEmail(), groupInfo.getId());

        // then
        assertThat(result).isTrue();
    }

    public static User generateUser() {

        return User.builder()
                .platformId("123456")
                .platformType(UserPlatformType.KAKAO)
                .role(UserRole.USER)
                .name("kms")
                .email("kms@kakao.com")
                .phoneNumber("010-0000-0000")
                .profileImageUrl("https://google.com")
                .pushAlarmYn(true)
                .build();
    }

    public static GroupInfo generateGroupInfo(User user) {

        return GroupInfo.builder()
                .user(user)
                .bgColor("bbbbbb")
                .colorCode("111111")
                .gradient("csscssscss")
                .name("group")
                .info("hello world!")
                .build();
    }

    public static GroupMember generateGroupMember(User user, GroupInfo groupInfo) {

        return GroupMember.builder()
                .groupInfo(groupInfo)
                .user(user)
                .role(GroupMemberRole.GROUP_MEMBER)
                .status(GroupMemberStatus.ACCEPT)
                .build();
    }
}