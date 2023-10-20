package com.heachi.housework.api.service.housework.info;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.group.member.GroupMemberException;
import com.heachi.admin.common.exception.housework.HouseworkException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.clients.auth.AuthClients;
import com.heachi.external.clients.auth.response.UserInfoResponse;
import com.heachi.housework.TestConfig;
import com.heachi.housework.api.service.auth.AuthExternalService;
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoCreateServiceRequest;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.GroupInfoRepository;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.category.repository.HouseworkCategoryRepository;
import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType;
import com.heachi.mysql.define.housework.info.repository.HouseworkInfoRepository;
import com.heachi.mysql.define.housework.member.repository.HouseworkMemberRepository;
import com.heachi.mysql.define.housework.todo.HouseworkTodo;
import com.heachi.mysql.define.housework.todo.repository.HouseworkTodoRepository;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class HouseworkInfoServiceTest extends TestConfig {

    @Autowired private GroupMemberRepository groupMemberRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private HouseworkInfoRepository houseworkInfoRepository;
    @Autowired private HouseworkInfoService houseworkInfoService;
    @Autowired private HouseworkCategoryRepository houseworkCategoryRepository;
    @Autowired private GroupInfoRepository groupInfoRepository;
    @Autowired private HouseworkMemberRepository houseworkMemberRepository;
    @Autowired private HouseworkTodoRepository houseworkTodoRepository;

    @AfterEach
    void tearDown() {
        houseworkMemberRepository.deleteAllInBatch();
        houseworkInfoRepository.deleteAllInBatch();
        groupMemberRepository.deleteAllInBatch();
        groupInfoRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        houseworkCategoryRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("집안일 추가 성공 테스트")
    void createHouseworkInfoSuccess() {
        // given
        HouseworkCategory category = generateHouseworkCategory();
        houseworkCategoryRepository.save(category);

        User user = generateUser();
        userRepository.save(user);

        GroupInfo groupInfo = generateGroupInfo(user);
        groupInfoRepository.save(groupInfo);

        GroupMember groupMember = generateGroupMember(user, groupInfo);
        groupMemberRepository.save(groupMember);

        List<Long> gmIdList = new ArrayList<>();
        gmIdList.add(groupMember.getId());

        HouseworkInfoCreateServiceRequest request = HouseworkInfoCreateServiceRequest.builder()
                .houseworkCategoryId(category.getId())
                .groupMemberIdList(gmIdList)
                .type(HouseworkPeriodType.HOUSEWORK_PERIOD_WEEK)
                .title("Test")
                .detail("Test")
                .dayDate(null)
                .weekDate("1")
                .monthDate(null)
                .endTime(LocalTime.now())
                .build();

        // when
        houseworkInfoService.createHouseworkInfo(request);
        HouseworkInfo savedHousework = houseworkInfoRepository.findByTitle(request.getTitle()).get();

        // then
        assertThat(savedHousework.getTitle()).isEqualTo(request.getTitle());
    }

    @Test
    @DisplayName("집안일 추가시 카테고리 조회에 실패한 경우 예외가 발생한다.")
    void createHouseworkInfoFailWhenNotFoundCategory() {
        // given
        User user = generateUser();
        userRepository.save(user);

        GroupInfo groupInfo = generateGroupInfo(user);
        groupInfoRepository.save(groupInfo);

        GroupMember groupMember = generateGroupMember(user, groupInfo);
        groupMemberRepository.save(groupMember);

        List<Long> gmIdList = new ArrayList<>();
        gmIdList.add(groupMember.getId());

        HouseworkInfoCreateServiceRequest request = HouseworkInfoCreateServiceRequest.builder()
                .houseworkCategoryId(3L)
                .groupMemberIdList(gmIdList)
                .type(HouseworkPeriodType.HOUSEWORK_PERIOD_WEEK)
                .title("Test")
                .detail("Test")
                .dayDate(null)
                .weekDate("1")
                .monthDate(null)
                .endTime(LocalTime.now())
                .build();

        // when & then
        HouseworkException exception = assertThrows(HouseworkException.class, () -> houseworkInfoService.createHouseworkInfo(request));
        assertEquals(exception.getMessage(), ExceptionMessage.HOUSEWORK_CATEGORY_NOT_FOUND.getText());
    }

    @Test
    @DisplayName("집안일 추가시 그룹 멤버 조회에 실패한 경우 예외가 발생한다.")
    void createHouseworkInfoFailWhenNotFoundGroupMember() {
        // given
        HouseworkCategory category = generateHouseworkCategory();
        houseworkCategoryRepository.save(category);

        User user = generateUser();
        userRepository.save(user);

        GroupInfo groupInfo = generateGroupInfo(user);
        groupInfoRepository.save(groupInfo);

        List<Long> idList = new ArrayList<>();
        idList.add(0L);

        HouseworkInfoCreateServiceRequest request = HouseworkInfoCreateServiceRequest.builder()
                .houseworkCategoryId(category.getId())
                .groupMemberIdList(idList)
                .type(HouseworkPeriodType.HOUSEWORK_PERIOD_WEEK)
                .title("Test")
                .detail("Test")
                .dayDate(null)
                .weekDate("1")
                .monthDate(null)
                .endTime(LocalTime.now())
                .build();

        // when & then
        GroupMemberException exception = assertThrows(GroupMemberException.class, () -> houseworkInfoService.createHouseworkInfo(request));
        assertEquals(exception.getMessage(), ExceptionMessage.GROUP_MEMBER_NOT_FOUND.getText());
    }

    @Test
    @DisplayName("딱 한번하는 집안일의 경우, HOUSEWORK_INFO가 생성되지 않고, HOUSEWORK_TODO가 바로 생성된다.")
    void createHouseworkInfoWhenPeriodDay() {
        // given
        HouseworkCategory category = generateHouseworkCategory();
        houseworkCategoryRepository.save(category);

        User user = generateUser();
        userRepository.save(user);

        GroupInfo groupInfo = generateGroupInfo(user);
        groupInfoRepository.save(groupInfo);

        GroupMember groupMember = generateGroupMember(user, groupInfo);
        groupMemberRepository.save(groupMember);

        HouseworkInfoCreateServiceRequest request = HouseworkInfoCreateServiceRequest.builder()
                .houseworkCategoryId(category.getId())
                .groupMemberIdList(null)
                .type(HouseworkPeriodType.HOUSEWORK_PERIOD_DAY) // 단건
                .title("Test")
                .detail("Test")
                .dayDate(LocalDate.now())
                .weekDate(null)
                .monthDate(null)
                .endTime(LocalTime.now())
                .build();

        // when
        houseworkInfoService.createHouseworkInfo(request);
        List<HouseworkInfo> infoList = houseworkInfoRepository.findAll();
        List<HouseworkTodo> todoList = houseworkTodoRepository.findAll();

        // then
        assertThat(infoList.size()).isEqualTo(0);
        assertThat(todoList.size()).isEqualTo(1);
    }
}