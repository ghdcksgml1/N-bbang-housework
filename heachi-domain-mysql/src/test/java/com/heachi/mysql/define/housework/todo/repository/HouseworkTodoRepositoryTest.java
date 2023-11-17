package com.heachi.mysql.define.housework.todo.repository;

import com.heachi.mysql.TestConfig;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.GroupInfoRepository;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.group.member.constant.GroupMemberRole;
import com.heachi.mysql.define.group.member.constant.GroupMemberStatus;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.category.repository.HouseworkCategoryRepository;
import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType;
import com.heachi.mysql.define.housework.info.repository.HouseworkInfoRepository;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import com.heachi.mysql.define.housework.member.repository.HouseworkMemberRepository;
import com.heachi.mysql.define.housework.todo.HouseworkTodo;
import com.heachi.mysql.define.housework.todo.repository.response.HouseworkTodoCount;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.mysql.define.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HouseworkTodoRepositoryTest extends TestConfig {

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
    @DisplayName("groupInfoId 리스트를 받아 해당 그룹들의 오늘 날짜의 HouseworkTodo의 개수를 셀 수 있는 DTO를 반환해준다.")
    void findHouseworkTodoCountByGroupInfoIdList() {
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
        List<HouseworkTodoCount> houseworkTodoCountByGroupInfoIdList = houseworkTodoRepository.findHouseworkTodoCountByGroupInfoIdList(List.of(groupInfo.getId(), groupInfo2.getId(), groupInfo3.getId()));

        // then
        assertThat(houseworkTodoCountByGroupInfoIdList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("todoId와 email을 통해 해당 그룹원이 해당 집안일의 담당자인지 확인한다.")
    void findHouseworkTodoByIdAndGroupMemberId() {
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
        HouseworkTodo findTodo = houseworkTodoRepository.findHouseworkTodoByIdAndGroupMemberId(houseworkTodo.getId(), groupMember.getId()).get();

        // then
        assertThat(findTodo.getHouseworkMember()).isEqualTo(String.valueOf(groupMember.getId()));
    }

    @Test
    @DisplayName("todoId로 조회는 되지만 groupMemberId가 맞지 않는 경우 Optional.empty()를 반환한다.")
    void findHouseworkTodoByIdAndGroupMemberIdFail() {
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
        var findTodo = houseworkTodoRepository.findHouseworkTodoByIdAndGroupMemberId(houseworkTodo.getId(), 2L);

        // then
        assertThat(findTodo.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("HouseworkInfoId에 맞는 Todo가 제대로 Null로 바뀌는지")
    void updateHouseworkTodoByHouseworkInfoId() {
        // given
        User user = userRepository.save(generateUser());
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(user));
        GroupMember groupMember = groupMemberRepository.save(generateGroupMember(user, groupInfo));

        HouseworkCategory houseworkCategory = houseworkCategoryRepository.save(generateHouseworkCategory());
        HouseworkInfo houseworkInfo = houseworkInfoRepository.save(generateHouseworkInfo(houseworkCategory));
        HouseworkInfo houseworkInfo2 = houseworkInfoRepository.save(generateHouseworkInfo(houseworkCategory));
        HouseworkInfo houseworkInfo3 = houseworkInfoRepository.save(generateHouseworkInfo(houseworkCategory));

        HouseworkMember houseworkMember = houseworkMemberRepository.save(generateHouseworkMember(groupMember, houseworkInfo));
        HouseworkInfo findHouseworkInfo = houseworkInfoRepository.findHouseworkInfoByIdJoinFetchHouseworkMembers(houseworkInfo.getId()).get();
        HouseworkInfo findHouseworkInfo2 = houseworkInfoRepository.findHouseworkInfoByIdJoinFetchHouseworkMembers(houseworkInfo2.getId()).get();
        HouseworkInfo findHouseworkInfo3 = houseworkInfoRepository.findHouseworkInfoByIdJoinFetchHouseworkMembers(houseworkInfo3.getId()).get();

        HouseworkTodo houseworkTodo = houseworkTodoRepository.save(generateHouseworkTodo(findHouseworkInfo, groupInfo, LocalDate.now()));
        HouseworkTodo houseworkTodo2 = houseworkTodoRepository.save(generateHouseworkTodo(findHouseworkInfo2, groupInfo, LocalDate.now()));
        HouseworkTodo houseworkTodo21 = houseworkTodoRepository.save(generateHouseworkTodo(findHouseworkInfo2, groupInfo, LocalDate.now()));
        HouseworkTodo houseworkTodo22 = houseworkTodoRepository.save(generateHouseworkTodo(findHouseworkInfo2, groupInfo, LocalDate.now()));
        HouseworkTodo houseworkTodo3 = houseworkTodoRepository.save(generateHouseworkTodo(findHouseworkInfo3, groupInfo, LocalDate.now()));

        // when
        houseworkTodoRepository.updateHouseworkTodoByHouseworkInfoId(houseworkTodo2.getId());
        List<HouseworkTodo> todoList = houseworkTodoRepository.findAll();

        // then
        assertThat(todoList.get(0).getHouseworkInfo()).isNotNull();
        assertThat(todoList.get(1).getHouseworkInfo()).isNull();
        assertThat(todoList.get(2).getHouseworkInfo()).isNull();
        assertThat(todoList.get(3).getHouseworkInfo()).isNull();
        assertThat(todoList.get(4).getHouseworkInfo()).isNotNull();
    }
}