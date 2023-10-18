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
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.mysql.define.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Test
    @DisplayName("GroupInfoId와 Date를 이용해 값을 맵으로 조회한다.")
    void findByGroupInfoAndDateReturnSetTest() {
        // given
        User user = userRepository.save(generateUser());
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(user));
        GroupMember groupMember = groupMemberRepository.save(generateGroupMember(user, groupInfo));

        HouseworkCategory houseworkCategory = houseworkCategoryRepository.save(generateHouseworkCategory());
        HouseworkInfo houseworkInfo = houseworkInfoRepository.save(generateHouseworkInfo(houseworkCategory));

        HouseworkMember houseworkMember = houseworkMemberRepository.save(generateHouseworkMember(groupMember, houseworkInfo));
        HouseworkInfo findHouseworkInfo = houseworkInfoRepository.findHouseworkInfoByIdJoinFetchHouseworkMembers(houseworkInfo.getId()).get();

        HouseworkTodo houseworkTodo = houseworkTodoRepository.save(generateHouseworkTodo(findHouseworkInfo, groupInfo, LocalDate.of(2022, 10, 10)));

        // when
        Map<Long, HouseworkTodo> result = houseworkTodoRepository.findByGroupInfoAndDate(groupInfo.getId(), LocalDate.of(2022, 10, 10))
                .stream()
                .collect(Collectors.toMap(obj -> obj.getId(), obj -> obj));

        // then
        assertThat(result.get(1L).getDate()).isEqualTo(LocalDate.of(2022, 10, 10));
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

    public static HouseworkCategory generateHouseworkCategory() {

        return HouseworkCategory.builder()
                .name("집안일")
                .build();
    }

    public static HouseworkInfo generateHouseworkInfo(HouseworkCategory category) {

        return HouseworkInfo.builder()
                .houseworkCategory(category)
                .title("빨래")
                .detail("빨래 돌리기")
                .type(HouseworkPeriodType.HOUSEWORK_PERIOD_EVERYDAY)
                .endTime(LocalTime.of(18,0))
                .build();
    }

    public static HouseworkMember generateHouseworkMember(GroupMember groupMember, HouseworkInfo houseworkInfo) {

        return HouseworkMember.builder()
                .houseworkInfo(houseworkInfo)
                .groupMember(groupMember)
                .build();
    }

    public static HouseworkTodo generateHouseworkTodo(HouseworkInfo houseworkInfo, GroupInfo groupInfo, LocalDate date) {

        return HouseworkTodo.makeTodoReferInfo(houseworkInfo, groupInfo, date);
    }
}