package com.heachi.mysql.define.group.info.repository;

import com.heachi.mysql.TestConfig;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.response.GroupInfoUserGroupResponse;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.category.repository.HouseworkCategoryRepository;
import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.info.repository.HouseworkInfoRepository;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import com.heachi.mysql.define.housework.member.repository.HouseworkMemberRepository;
import com.heachi.mysql.define.housework.todo.HouseworkTodo;
import com.heachi.mysql.define.housework.todo.repository.HouseworkTodoRepository;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GroupInfoRepositoryImplTest extends TestConfig {

    @Autowired private GroupMemberRepository groupMemberRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private GroupInfoRepository groupInfoRepository;
    @Autowired private HouseworkCategoryRepository houseworkCategoryRepository;
    @Autowired private HouseworkInfoRepository houseworkInfoRepository;
    @Autowired private HouseworkTodoRepository houseworkTodoRepository;
    @Autowired private HouseworkMemberRepository houseworkMemberRepository;

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
    @DisplayName("유저 이메일을 통해 해당 유저가 속한 그룹의 정보들을 가져와 주는 쿼리")
    void findGroupInfoUserGroupResponseListByUserEmail() {
        // given
        User user = userRepository.save(generateUser());
        User user2 = userRepository.save(generateCustomUser("ghdcksgml1@naver.com", "010-1111-1111"));
        User user3 = userRepository.save(generateCustomUser("ghdcksgml2@naver.com", "010-2222-2222"));
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(user));
        GroupInfo groupInfo2 = groupInfoRepository.save(generateGroupInfo(user2));
        GroupInfo groupInfo3 = groupInfoRepository.save(generateGroupInfo(user3));
        GroupMember groupMember = groupMemberRepository.save(generateGroupMember(user, groupInfo));
        groupMemberRepository.save(generateGroupMember(user2, groupInfo));
        groupMemberRepository.save(generateGroupMember(user3, groupInfo));
        groupMemberRepository.save(generateGroupMember(user, groupInfo3));
        groupMemberRepository.save(generateGroupMember(user3, groupInfo2));

        HouseworkCategory houseworkCategory = houseworkCategoryRepository.save(generateHouseworkCategory());
        HouseworkInfo houseworkInfo = houseworkInfoRepository.save(generateHouseworkInfo(houseworkCategory));
        HouseworkInfo houseworkInfo2 = houseworkInfoRepository.save(generateHouseworkInfo(houseworkCategory));
        HouseworkInfo houseworkInfo3 = houseworkInfoRepository.save(generateHouseworkInfo(houseworkCategory));

        HouseworkMember houseworkMember = houseworkMemberRepository.save(generateHouseworkMember(groupMember, houseworkInfo));
        HouseworkInfo findHouseworkInfo = houseworkInfoRepository.findHouseworkInfoByIdJoinFetchHouseworkMembers(houseworkInfo.getId()).get();
        HouseworkInfo findHouseworkInfo2 = houseworkInfoRepository.findHouseworkInfoByIdJoinFetchHouseworkMembers(houseworkInfo2.getId()).get();
        HouseworkInfo findHouseworkInfo3 = houseworkInfoRepository.findHouseworkInfoByIdJoinFetchHouseworkMembers(houseworkInfo3.getId()).get();

        HouseworkTodo houseworkTodo = houseworkTodoRepository.save(generateHouseworkTodo(findHouseworkInfo, groupInfo, LocalDate.now()));
        HouseworkTodo houseworkTodo2 = houseworkTodoRepository.save(generateHouseworkTodo(findHouseworkInfo2, groupInfo2, LocalDate.now()));
        HouseworkTodo houseworkTodo3 = houseworkTodoRepository.save(generateHouseworkTodo(findHouseworkInfo3, groupInfo3, LocalDate.now()));

        // when
        var userGroup = groupInfoRepository.findGroupInfoUserGroupResponseListByUserEmail(user.getEmail());
        var user2Group = groupInfoRepository.findGroupInfoUserGroupResponseListByUserEmail(user2.getEmail());
        var user3Group = groupInfoRepository.findGroupInfoUserGroupResponseListByUserEmail(user3.getEmail());

        // then
        assertThat(userGroup.size()).isEqualTo(2);
        assertThat(user2Group.size()).isEqualTo(1);
        assertThat(user3Group.size()).isEqualTo(2);
    }
}