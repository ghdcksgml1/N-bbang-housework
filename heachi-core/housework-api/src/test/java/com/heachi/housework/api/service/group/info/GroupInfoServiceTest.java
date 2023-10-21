package com.heachi.housework.api.service.group.info;

import com.heachi.admin.common.exception.user.UserException;
import com.heachi.housework.TestConfig;
import com.heachi.housework.api.service.group.info.request.GroupInfoCreateServiceRequest;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.GroupInfoRepository;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GroupInfoServiceTest extends TestConfig {

    @Autowired private UserRepository userRepository;
    @Autowired private GroupInfoRepository groupInfoRepository;
    @Autowired private GroupMemberRepository groupMemberRepository;

    @Autowired private GroupInfoService groupInfoService;

    @AfterEach
    void tearDown() {
        groupMemberRepository.deleteAllInBatch();
        groupInfoRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("올바른 GroupInfoCreateServiceRequest를 넘기면, 요청한 유저가 관리자로 GroupInfo, GroupMember가 생성된다.")
    void createGroupInfoSuccess() {
        // given
        User user = userRepository.save(generateUser());
        var request = GroupInfoCreateServiceRequest.builder()
                .bgColor("bgColor")
                .colorCode("colorCode")
                .gradient("gradient")
                .name("name")
                .info("info")
                .email(user.getEmail())
                .build();

        // when
        groupInfoService.createGroupInfo(request);
        GroupInfo groupInfo = groupInfoRepository.findAll().get(0);
        GroupMember groupMember = groupMemberRepository.findAll().get(0);

        // then
        assertThat(groupInfo.getUser().getId()).isEqualTo(user.getId());
        assertThat(groupMember.getGroupInfo().getId()).isEqualTo(groupInfo.getId());
    }

    @Test
    @DisplayName("존재하지 않는 유저 이메일일 경우, GroupInfo를 생성할 수 없다.")
    void createGroupInfoFailWhenUserEmailNotFound() {
        // given
        var request = GroupInfoCreateServiceRequest.builder()
                .bgColor("bgColor")
                .colorCode("colorCode")
                .gradient("gradient")
                .name("name")
                .info("info")
                .email("kms@kakao.com")
                .build();

        // when & then
        assertThrows(UserException.class ,() -> groupInfoService.createGroupInfo(request));

        List<GroupInfo> groupInfoList = groupInfoRepository.findAll();
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();

        assertThat(groupInfoList.size()).isEqualTo(0);
        assertThat(groupMemberList.size()).isEqualTo(0);
    }
}