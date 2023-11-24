package com.heachi.housework.api.service.group.info;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.group.info.GroupInfoException;
import com.heachi.admin.common.exception.group.member.GroupMemberException;
import com.heachi.housework.TestConfig;
import com.heachi.housework.api.controller.group.info.request.GroupInfoRegisterRequest;
import com.heachi.housework.api.controller.group.info.request.GroupInfoRegisterRequestStatusEnum;
import com.heachi.housework.api.service.group.info.request.GroupInfoUpdateServiceRequest;
import com.heachi.housework.api.service.group.info.response.GroupInfoUpdatePageResponse;
import com.heachi.housework.api.service.group.info.response.GroupInfoUserGroupServiceResponse;
import com.heachi.admin.common.exception.user.UserException;
import com.heachi.housework.TestConfig;
import com.heachi.housework.api.service.group.info.request.GroupInfoCreateServiceRequest;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.GroupInfoRepository;
import com.heachi.mysql.define.group.info.repository.response.GroupInfoUserGroupResponse;
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
import com.heachi.mysql.define.housework.todo.constant.HouseworkTodoStatus;
import com.heachi.mysql.define.housework.todo.repository.HouseworkTodoRepository;

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
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GroupInfoServiceTest extends TestConfig {

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
    private GroupInfoService groupInfoService;

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
    @DisplayName("유저의 email을 통해 유저가 속한 그룹과 각 그룹의 멤버, 해당 날짜의 그룹별 집안일 진행상황을 나타낸다.")
    void userGroupInfoList() {
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
        List<GroupInfoUserGroupServiceResponse> groupServiceResponses = groupInfoService.userGroupInfoList(user.getEmail());
        groupServiceResponses.forEach(System.out::println);

        // then
        assertThat(groupServiceResponses.get(0).getGroupMembers().size()).isEqualTo(3);
        assertThat(groupServiceResponses.get(0).getRemainTodoListCnt()).isEqualTo(1);
        assertThat(groupServiceResponses.get(0).getProgressPercent()).isEqualTo(0);

        assertThat(groupServiceResponses.get(1).getGroupMembers().size()).isEqualTo(1);
        assertThat(groupServiceResponses.get(1).getRemainTodoListCnt()).isEqualTo(1);
        assertThat(groupServiceResponses.get(1).getProgressPercent()).isEqualTo(0);
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
        assertThrows(UserException.class, () -> groupInfoService.createGroupInfo(request));

        List<GroupInfo> groupInfoList = groupInfoRepository.findAll();
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();

        assertThat(groupInfoList.size()).isEqualTo(0);
        assertThat(groupMemberList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("사용자가 해당 그룹에 가입한 이력이 없는경우 성공적으로 그룹멤버로 WAITING인 상태로 추가된다.")
    void joinGroupInfo() {
        // given
        // 그룹 생성
        User user = userRepository.save(generateUser());
        var request = GroupInfoCreateServiceRequest.builder()
                .bgColor("bgColor")
                .colorCode("colorCode")
                .gradient("gradient")
                .name("name")
                .info("info")
                .email(user.getEmail())
                .build();
        groupInfoService.createGroupInfo(request);
        GroupInfo groupInfo = groupInfoRepository.findAll().get(0);

        User user2 = userRepository.save(generateCustomUser("ghdcksgml1@naver.com", "010-1111-1111"));

        // when
        groupInfoService.joinGroupInfo(user2.getEmail(), groupInfo.getJoinCode());
        GroupMember findGroupMember = groupMemberRepository.findByUserAndGroupInfo(user2, groupInfo).get();

        // then
        assertThat(findGroupMember.getGroupInfo().getId()).isEqualTo(groupInfo.getId());
        assertThat(findGroupMember.getUser().getId()).isEqualTo(user2.getId());
        assertThat(findGroupMember.getRole()).isEqualTo(GroupMemberRole.GROUP_MEMBER);
        assertThat(findGroupMember.getStatus()).isEqualTo(GroupMemberStatus.WAITING);
    }

    @Test
    @DisplayName("사용자가 해당 그룹에 이미 가입해있거나, 신청 대기중일 경우 아무런 동작도 일어나지 않는다.")
    void joinGroupInfoAlreadyGroupMemberStatusWAITING() {
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
        groupInfoService.createGroupInfo(request);
        GroupInfo groupInfo = groupInfoRepository.findAll().get(0);

        // when
        groupInfoService.joinGroupInfo(user.getEmail(), groupInfo.getJoinCode());
        GroupMember findGroupMember = groupMemberRepository.findByUserAndGroupInfo(user, groupInfo).get();

        // then
        assertThat(findGroupMember.getStatus()).isEqualTo(GroupMemberStatus.ACCEPT);
        assertThat(findGroupMember.getRole()).isEqualTo(GroupMemberRole.GROUP_ADMIN);
    }

    @Test
    @DisplayName("사용자가 해당 그룹에 재가입하는 경우(기존 상태 WITHDRAW) 역할은 GROUP_MEMBER, 상태는 WAITING으로 그룹 가입 신청이 된다.")
    void joinGroupInfoAlreadyGroupMemberStatusWITHDRAW() {
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
        groupInfoService.createGroupInfo(request);
        GroupInfo groupInfo = groupInfoRepository.findAll().get(0);

        User user2 = userRepository.save(generateCustomUser("ghdcksgml1@naver.com", "010-1111-1111"));
        groupMemberRepository.save(GroupMember.builder()
                .groupInfo(groupInfo)
                .user(user2)
                .role(GroupMemberRole.GROUP_ADMIN) // 기존에 관리자였던 멤버
                .status(GroupMemberStatus.WITHDRAW)
                .build());

        // when
        groupInfoService.joinGroupInfo(user2.getEmail(), groupInfo.getJoinCode());
        GroupMember findGroupMember = groupMemberRepository.findByUserAndGroupInfo(user2, groupInfo).get();

        // then
        assertThat(findGroupMember.getStatus()).isEqualTo(GroupMemberStatus.WAITING);
        assertThat(findGroupMember.getRole()).isEqualTo(GroupMemberRole.GROUP_MEMBER);
    }

    @Test
    @DisplayName("그룹 가입 요청자의 상태가 WAITING이 아닐 경우 예외가 발생한다.")
    void joinRequestGroupMemberStatusIsNotWAITING() {
        // given
        // Admin
        User admin = userRepository.save(generateUser());
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(admin));

        GroupMember adminMember = GroupMember.builder()
                .groupInfo(groupInfo)
                .role(GroupMemberRole.GROUP_ADMIN)
                .status(GroupMemberStatus.ACCEPT)
                .user(admin)
                .build();
        groupMemberRepository.save(adminMember);

        // WAITING 상태가 아닌 그룹 멤버
        User requestUser = userRepository.save(User.builder()
                .platformId("11")
                .platformType(UserPlatformType.KAKAO)
                .role(UserRole.USER)
                .name("test")
                .email("test@test.com")
                .phoneNumber("010-1233-0000")
                .profileImageUrl("https://google.com")
                .pushAlarmYn(true)
                .build());
        GroupMember requestMember = GroupMember.builder()
                .groupInfo(groupInfo)
                .role(GroupMemberRole.GROUP_MEMBER)
                .status(GroupMemberStatus.ACCEPT)
                .user(requestUser)
                .build();
        GroupMember requestWaitingMember = groupMemberRepository.save(requestMember);

        GroupInfoRegisterRequest request = GroupInfoRegisterRequest.builder()
                .groupMemberId(requestWaitingMember.getId())
                .groupId(groupInfo.getId())
                .status(GroupInfoRegisterRequestStatusEnum.REFUSE)
                .build();

        // when & then
        GroupMemberException exception = assertThrows(GroupMemberException.class, () -> groupInfoService.joinRequestHandler(admin.getEmail(), request));
        assertThat(exception.getMessage()).isEqualTo(ExceptionMessage.GROUP_MEMBER_STATUS_NOT_WAITING.getText());

    }

    @Test
    @DisplayName("해당 그룹의 그룹장이 아닐 경우 예외가 발생한다.")
    void joinResponseGroupMemberRoleIsNotAdmin() {
        // given
        // Admin이 아닌 그룹 멤버
        User notAdmin = userRepository.save(User.builder()
                .platformId("11")
                .platformType(UserPlatformType.KAKAO)
                .role(UserRole.USER)
                .name("test")
                .email("test@test.com")
                .phoneNumber("010-1233-0000")
                .profileImageUrl("https://google.com")
                .pushAlarmYn(true)
                .build());
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(notAdmin));
        groupMemberRepository.save(generateGroupMember(notAdmin, groupInfo));

        // WAITING 상태의 그룹 멤버
        User requestUser = userRepository.save(generateUser());
        GroupMember requestMember = GroupMember.builder()
                .groupInfo(groupInfo)
                .role(GroupMemberRole.GROUP_MEMBER)
                .status(GroupMemberStatus.WAITING)
                .user(requestUser)
                .build();
        GroupMember requestWaitingMember = groupMemberRepository.save(requestMember);

        GroupInfoRegisterRequest request = GroupInfoRegisterRequest.builder()
                .groupMemberId(requestWaitingMember.getId())
                .groupId(groupInfo.getId())
                .status(GroupInfoRegisterRequestStatusEnum.ACCEPT)
                .build();

        // when & then
        GroupMemberException exception = assertThrows(GroupMemberException.class, () -> groupInfoService.joinRequestHandler(notAdmin.getEmail(), request));
        assertThat(exception.getMessage()).isEqualTo(ExceptionMessage.GROUP_MEMBER_ROLE_NOT_ADMIN.getText());

    }

    @Test
    @DisplayName("status가 true일 경우 그룹 가입 요청이 승인되어 그룹 가입 요청자의 상태가 ACCEPT로 변경된다.")
    void joinRequestStatusIsTrue() {
        // Admin
        User admin = userRepository.save(generateUser());
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(admin));

        GroupMember adminMember = GroupMember.builder()
                .groupInfo(groupInfo)
                .role(GroupMemberRole.GROUP_ADMIN)
                .status(GroupMemberStatus.ACCEPT)
                .user(admin)
                .build();
        groupMemberRepository.save(adminMember);

        // WAITING 상태의 그룹 멤버
        User requestUser = userRepository.save(User.builder()
                .platformId("11")
                .platformType(UserPlatformType.KAKAO)
                .role(UserRole.USER)
                .name("test")
                .email("test@test.com")
                .phoneNumber("010-1233-0000")
                .profileImageUrl("https://google.com")
                .pushAlarmYn(true)
                .build());
        GroupMember requestMember = GroupMember.builder()
                .groupInfo(groupInfo)
                .role(GroupMemberRole.GROUP_MEMBER)
                .status(GroupMemberStatus.WAITING)
                .user(requestUser)
                .build();
        GroupMember requestWaitingMember = groupMemberRepository.save(requestMember);

        GroupInfoRegisterRequest request = GroupInfoRegisterRequest.builder()
                .groupMemberId(requestWaitingMember.getId())
                .groupId(groupInfo.getId())
                .status(GroupInfoRegisterRequestStatusEnum.ACCEPT)
                .build();

        // when
        groupInfoService.joinRequestHandler(admin.getEmail(), request);


        // then
        GroupMember updateMember = groupMemberRepository.findById(requestWaitingMember.getId()).get();
        assertThat(updateMember.getStatus()).isEqualTo(GroupMemberStatus.ACCEPT);
    }

    @Test
    @DisplayName("status가 false일 경우 그룹 가입 요청이 거절되어 그룹 가입 요청자의 상태가 WITHDRAW로 변경된다.")
    void joinRequestStatusIsFalse() {
        // Admin
        User admin = userRepository.save(generateUser());
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(admin));

        GroupMember adminMember = GroupMember.builder()
                .groupInfo(groupInfo)
                .role(GroupMemberRole.GROUP_ADMIN)
                .status(GroupMemberStatus.ACCEPT)
                .user(admin)
                .build();
        groupMemberRepository.save(adminMember);

        // WAITING 상태의 그룹 멤버
        User requestUser = userRepository.save(User.builder()
                .platformId("11")
                .platformType(UserPlatformType.KAKAO)
                .role(UserRole.USER)
                .name("test")
                .email("test@test.com")
                .phoneNumber("010-1233-0000")
                .profileImageUrl("https://google.com")
                .pushAlarmYn(true)
                .build());
        GroupMember requestMember = GroupMember.builder()
                .groupInfo(groupInfo)
                .role(GroupMemberRole.GROUP_MEMBER)
                .status(GroupMemberStatus.WAITING)
                .user(requestUser)
                .build();
        GroupMember requestWaitingMember = groupMemberRepository.save(requestMember);

        GroupInfoRegisterRequest request = GroupInfoRegisterRequest.builder()
                .groupMemberId(requestWaitingMember.getId())
                .groupId(groupInfo.getId())
                .status(GroupInfoRegisterRequestStatusEnum.REFUSE)
                .build();

        // when
        groupInfoService.joinRequestHandler(admin.getEmail(), request);


        // then
        GroupMember updateMember = groupMemberRepository.findById(requestWaitingMember.getId()).get();
        assertThat(updateMember.getStatus()).isEqualTo(GroupMemberStatus.WITHDRAW);
    }

    @Test
    @DisplayName("그룹 수정 페이지를 성공적으로 불러온다.")
    void groupInfoUpdatePageTest() {
        // given
        User user = userRepository.save(generateUser());
        GroupInfo group = groupInfoRepository.save(generateGroupInfo(user));

        // when
        GroupInfoUpdatePageResponse response = groupInfoService.updateGroupInfoPage(group.getId());

        // then
        assertThat(response.getName()).isEqualTo("group");
        assertThat(response.getInfo()).isEqualTo("hello world!");
    }

    @Test
    @DisplayName("그룹 정보 수정을 요청했을 경우 성공적으로 수정된다.")
    void groupInfoUpdateTest() {
        // given
        User user = userRepository.save(generateUser());
        GroupInfo group = groupInfoRepository.save(generateGroupInfo(user));

        GroupInfoUpdateServiceRequest request = GroupInfoUpdateServiceRequest.builder()
                .groupId(group.getId())
                .bgColor("updateBG")
                .colorCode("updateColor")
                .gradient("updateGradient")
                .name("updateName")
                .info("updateInfo")
                .build();

        // when
        groupInfoService.updateGroupInfo(request);

        // then
        GroupInfo findGroup = groupInfoRepository.findById(group.getId()).get();
        assertThat(findGroup.getBgColor()).isEqualTo("updateBG");
        assertThat(findGroup.getColorCode()).isEqualTo("updateColor");
        assertThat(findGroup.getGradient()).isEqualTo("updateGradient");
        assertThat(findGroup.getName()).isEqualTo("updateName");
        assertThat(findGroup.getInfo()).isEqualTo("updateInfo");
    }

    @Test
    @DisplayName("그룹 정보 삭제를 요청했을 경우 성공적으로 수정된다.")
    void groupInfoDeleteTest() {
        // given
        User user = userRepository.save(generateCustomUser("lee@naver.com", "010-1111-1111"));
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(user));
        GroupMember gm1 = groupMemberRepository.save(generateGroupMember(user, groupInfo));

        HouseworkCategory houseworkCategory = houseworkCategoryRepository.save(generateHouseworkCategory());
        HouseworkInfo houseworkInfo = houseworkInfoRepository.save(HouseworkInfo.builder()
                .groupInfo(groupInfo)
                .houseworkCategory(houseworkCategory)
                .title("빨래")
                .detail("빨래하기")
                .groupInfo(groupInfo)
                .type(HouseworkPeriodType.HOUSEWORK_PERIOD_DAY)
                .dayDate(LocalDate.now())
                .endTime(LocalTime.now())
                .build());
        HouseworkInfo houseworkInfo2 = houseworkInfoRepository.save(HouseworkInfo.builder()
                .groupInfo(groupInfo)
                .houseworkCategory(houseworkCategory)
                .title("청소")
                .detail("청소하기")
                .groupInfo(groupInfo)
                .type(HouseworkPeriodType.HOUSEWORK_PERIOD_EVERYDAY)
                .endTime(LocalTime.now())
                .build());

        HouseworkMember h1 = houseworkMemberRepository.save(generateHouseworkMember(gm1, houseworkInfo));
        HouseworkMember h2 = houseworkMemberRepository.save(generateHouseworkMember(gm1, houseworkInfo2));

        // 오늘 날짜의 단건 집안일 생성
        HouseworkTodo saveTodo = houseworkTodoRepository.save(HouseworkTodo.builder()
                .houseworkInfo(null) // 단건은 HouseworkInfo가 존재하지 않는다.
                .groupInfo(groupInfo)
                .houseworkMember(h1.toString())
                .category(houseworkCategory.getName())
                .title("빨래")
                .detail("빨래하기")
                .status(HouseworkTodoStatus.HOUSEWORK_TODO_INCOMPLETE)
                .endTime(LocalTime.now())
                .date(LocalDate.now())
                .build());

        // 오늘 날짜의 비단건 집안일 생성
        HouseworkTodo saveTodo2 = houseworkTodoRepository.save(HouseworkTodo.builder()
                .houseworkInfo(houseworkInfo2)
                .groupInfo(groupInfo)
                .houseworkMember(h2.toString())
                .category(houseworkCategory.getName())
                .title("청소")
                .detail("청소하기")
                .status(HouseworkTodoStatus.HOUSEWORK_TODO_COMPLETE)
                .endTime(LocalTime.now())
                .date(LocalDate.now())
                .build());

        List<HouseworkTodo> houseworkTodos = houseworkTodoRepository.findByGroupInfoAndDate(groupInfo.getId(), LocalDate.now());
        System.out.println("=======" + houseworkTodos.size());

        // when
        groupInfoService.deleteGroupInfo(groupInfo.getId());

        // then
        // user의 groupInfoList에서 삭제되었는지 확인
        User findUser = userRepository.findByEmail(user.getEmail()).get();
        List<GroupInfoUserGroupResponse> list = groupInfoRepository.findGroupInfoUserGroupResponseListByUserEmail(findUser.getEmail());
        assertThat(list.contains(groupInfo)).isFalse();

        // HouseworkTodo 삭제되었는지 확인
        assertThat(houseworkTodoRepository.findById(saveTodo.getId()).orElse(null)).isNull();
        assertThat(houseworkTodoRepository.findById(saveTodo2.getId()).orElse(null)).isNull();

        // HouseworkMember 삭제되었는지 확인
        assertThat(houseworkMemberRepository.findById(h1.getId()).orElse(null)).isNull();
        assertThat(houseworkMemberRepository.findById(h2.getId()).orElse(null)).isNull();

        // HouseworkInfo 삭제되었는지 확인
        assertThat(houseworkInfoRepository.findById(houseworkInfo.getId()).orElse(null)).isNull();
        assertThat(houseworkInfoRepository.findById(houseworkInfo2.getId()).orElse(null)).isNull();

        // GroupMember 삭제되었는지 확인
        assertThat(groupMemberRepository.findById(gm1.getId()).orElse(null)).isNull();

        // GroupInfo 삭제되었는지 확인
        assertThat(groupInfoRepository.findById(groupInfo.getId()).orElse(null)).isNull();
    }
}