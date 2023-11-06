package com.heachi.housework.api.service.group.member;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.group.member.GroupMemberException;
import com.heachi.housework.TestConfig;
import com.heachi.housework.api.controller.group.member.response.GroupMemberResponse;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.GroupInfoRepository;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import com.heachi.mysql.define.housework.category.repository.HouseworkCategoryRepository;
import com.heachi.mysql.define.housework.info.repository.HouseworkInfoRepository;
import com.heachi.mysql.define.housework.member.repository.HouseworkMemberRepository;
import com.heachi.mysql.define.housework.todo.repository.HouseworkTodoRepository;
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
class GroupMemberServiceTest extends TestConfig {
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupInfoRepository groupInfoRepository;
    @Autowired
    private HouseworkCategoryRepository houseworkCategoryRepository;
    @Autowired
    private HouseworkInfoRepository houseworkInfoRepository;
    @Autowired
    private HouseworkTodoRepository houseworkTodoRepository;
    @Autowired
    private HouseworkMemberRepository houseworkMemberRepository;

    @Autowired
    private GroupMemberService groupMemberService;

    @AfterEach
    void tearDown() {
        houseworkTodoRepository.deleteAllInBatch();
        houseworkMemberRepository.deleteAllInBatch();
        houseworkInfoRepository.deleteAllInBatch();
        houseworkCategoryRepository.deleteAllInBatch();
        groupMemberRepository.deleteAllInBatch();
        groupInfoRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName(" groupMemberList API 호출시 GroupMemberListResponse 리스트가 잘 반환된다.\n")
    void selectGroupMemberListSuccessTest() {
        // given
        User user = userRepository.save(generateUser()); // 그룹장
        User user2 = userRepository.save(generateCustomUser("lee@naver.com", "010-1111-1111"));
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(user));
        GroupMember groupMember = groupMemberRepository.save(generateGroupMember(user, groupInfo));
        GroupMember groupMember2 = groupMemberRepository.save(generateGroupMember(user2, groupInfo));

        // when
        List<GroupMemberResponse> groupMemberRespons = groupMemberService.selctGroupMember(groupInfo.getId());

        // then
        assertThat(groupMemberRespons.size()).isEqualTo(2);
        assertThat(groupMemberRespons.get(0).getGroupMemberId()).isEqualTo(groupMember.getId());
        assertThat(groupMemberRespons.get(1).getGroupMemberId()).isEqualTo(groupMember2.getId());

    }
}