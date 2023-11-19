package com.heachi.housework.api.service.housework.info;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.HeachiException;
import com.heachi.admin.common.exception.group.member.GroupMemberException;
import com.heachi.admin.common.exception.housework.HouseworkException;
import com.heachi.admin.common.utils.DayOfWeekUtils;
import com.heachi.housework.TestConfig;
import com.heachi.housework.api.controller.housework.info.request.HouseworkInfoDeleteType;
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoCreateServiceRequest;
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoDeleteRequest;
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoUpdateServiceRequest;
import com.heachi.housework.api.service.housework.info.response.HouseworkInfoUpdatePageResponse;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.GroupInfoRepository;
import com.heachi.mysql.define.group.member.GroupMember;
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
import com.heachi.mysql.define.user.repository.UserRepository;
import com.heachi.redis.define.housework.todo.Todo;
import com.heachi.redis.define.housework.todo.TodoList;
import com.heachi.redis.define.housework.todo.repository.TodoListRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class HouseworkInfoServiceTest extends TestConfig {

    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HouseworkInfoRepository houseworkInfoRepository;
    @Autowired
    private HouseworkInfoService houseworkInfoService;
    @Autowired
    private HouseworkCategoryRepository houseworkCategoryRepository;
    @Autowired
    private GroupInfoRepository groupInfoRepository;
    @Autowired
    private HouseworkMemberRepository houseworkMemberRepository;
    @Autowired
    private HouseworkTodoRepository houseworkTodoRepository;

    @Autowired
    private TodoListRepository todoListRepository;

    @AfterEach
    void tearDown() {
        System.out.println("\n====== 테스트를 종료합니다 ======");
        houseworkTodoRepository.deleteAllInBatch();
        houseworkMemberRepository.deleteAllInBatch();
        houseworkInfoRepository.deleteAllInBatch();
        groupMemberRepository.deleteAllInBatch();
        groupInfoRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        houseworkCategoryRepository.deleteAllInBatch();

        todoListRepository.deleteAll();
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
                .groupId(groupInfo.getId())
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
                .groupId(groupInfo.getId())
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
                .groupId(groupInfo.getId())
                .build();

        // when & then
        assertThrows(GroupMemberException.class, () -> houseworkInfoService.createHouseworkInfo(request));
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
                .groupId(groupInfo.getId())
                .build();

        // when
        houseworkInfoService.createHouseworkInfo(request);
        List<HouseworkInfo> infoList = houseworkInfoRepository.findAll();
        List<HouseworkTodo> todoList = houseworkTodoRepository.findAll();

        // then
        assertThat(infoList.size()).isEqualTo(0);
        assertThat(todoList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("딱 한번하는 집안일의 경우, HOUSEWORK_INFO가 생성되지 않고, HOUSEWORK_TODO가 바로 생성된다. " +
            "만약, Redis에 이미 캐싱되어있던 TodoList일 경우 DirtyBit가 Check로 바뀐다.")
    void createHouseworkInfoWhenPeriodDayDirtyBitChecking() {
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
                .groupId(groupInfo.getId())
                .build();

        TodoList todoList = todoListRepository.save(TodoList.builder()
                .groupInfoId(groupInfo.getId())
                .date(LocalDate.now())
                .build());

        // when
        houseworkInfoService.createHouseworkInfo(request);
        TodoList savedTodoList = todoListRepository.findById(todoList.getId()).get();

        // then
        assertThat(savedTodoList.isDirtyBit()).isTrue();
    }

    @Test
    @DisplayName("주마다 하는 집안일의 경우, HOUSEWORK_INFO가 생성되고, " +
            "이미 캐싱되어 있는 TodoList의 Date와 주마다 하는 집안일의 주기가 겹칠 경우 dirtyBit가 Checking된다.")
    void createHouseworkInfoWhenPeriodWeekDirtyBitChecking() {
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
                .type(HouseworkPeriodType.HOUSEWORK_PERIOD_WEEK) // 주마다
                .title("Test")
                .detail("Test")
                .dayDate(null)
                .weekDate("1,2,3,4,5") // 월,화,수,목,금 마다
                .monthDate(null)
                .endTime(LocalTime.now())
                .groupId(groupInfo.getId())
                .build();

        TodoList todoList = todoListRepository.save(TodoList.builder()
                .groupInfoId(groupInfo.getId())
                .date(LocalDate.of(2023, 10, 27)) // 금요일이니까 bitChecking 돼야함
                .build());
        TodoList todoList2 = todoListRepository.save(TodoList.builder()
                .groupInfoId(groupInfo.getId())
                .date(LocalDate.of(2023, 10, 25)) // 수요일이니까 bitChecking 돼야함
                .build());
        TodoList todoList3 = todoListRepository.save(TodoList.builder()
                .groupInfoId(100L)
                .date(LocalDate.of(2023, 10, 25)) // 주기에 해당되지만, GroupInfoId가 다르다.
                .build());

        // when
        houseworkInfoService.createHouseworkInfo(request);
        var findTodoList = todoListRepository.findByGroupInfoId(groupInfo.getId());
        var findTodoList2 = todoListRepository.findByGroupInfoId(100L);

        // then
        assertThat(findTodoList.get(0).getId()).isEqualTo(todoList.getId()); // 이전에 생성되어 있던 todoList가 맞는지
        assertThat(findTodoList.get(0).isDirtyBit()).isTrue(); // 비트 체킹

        assertThat(findTodoList.get(1).getId()).isEqualTo(todoList2.getId()); // 이전에 생성되어 있던 todoList가 맞는지
        assertThat(findTodoList.get(1).isDirtyBit()).isTrue(); // 비트 체킹

        assertThat(findTodoList2.get(0).isDirtyBit()).isFalse();
    }

    @Test
    @DisplayName("달마다 하는 집안일의 경우, HOUSEWORK_INFO가 생성되고, " +
            "이미 캐싱되어 있는 TodoList의 Date와 달마다 하는 집안일의 주기가 겹칠 경우 dirtyBit가 Checking된다.")
    void createHouseworkInfoWhenPeriodMonthDirtyBitChecking() {
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
                .type(HouseworkPeriodType.HOUSEWORK_PERIOD_MONTH) // 주마다
                .title("Test")
                .detail("Test")
                .dayDate(null)
                .weekDate(null)
                .monthDate("12,23,30") // 12, 23, 30일 마다
                .endTime(LocalTime.now())
                .groupId(groupInfo.getId())
                .build();

        TodoList todoList = todoListRepository.save(TodoList.builder()
                .groupInfoId(groupInfo.getId())
                .date(LocalDate.of(2023, 10, 23)) // 23일이니까 비트체킹 돼야함.
                .build());

        // when
        houseworkInfoService.createHouseworkInfo(request);
        var findTodoList = todoListRepository.findByGroupInfoId(groupInfo.getId());

        // then
        assertThat(findTodoList.get(0).getId()).isEqualTo(todoList.getId());
        assertThat(findTodoList.get(0).isDirtyBit()).isTrue();
    }

    @Test
    @DisplayName("단건 집안일을 삭제하고 싶을 경우, 해당 HouseworkTodo의 status가 DELETE로 변경되고 " +
            "요청 날짜 TodoList의 dirtyBit이 true가 된다.")
    void deleteHouseworkInfoWhenPeriodDay() {
        /*
            given
        */
        // User 2명 생성 - A, B
        User A = userRepository.save(generateCustomUser("aaa@naver.com", "010-0000-0000"));
        User B = userRepository.save(generateCustomUser("bbb@naver.com", "010-1111-1111"));

        // 그룹 생성 - A가 그룹장
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(A));
        // 그룹 멤버 생성 - A, B
        GroupMember gA = groupMemberRepository.save(generateGroupMember(A, groupInfo));
        GroupMember gB = groupMemberRepository.save(generateGroupMember(B, groupInfo));

        // 카테고리 2개 생성 - 집안일, 집 밖의 일
        HouseworkCategory inWork = houseworkCategoryRepository.save(generateCustomHouseworkCategory("집안일"));
        HouseworkCategory outWork = houseworkCategoryRepository.save(generateCustomHouseworkCategory("집 밖의 일"));

        // 단건 집안일 생성
        HouseworkTodo saveTodo = houseworkTodoRepository.save(HouseworkTodo.builder()
                .houseworkInfo(null) // 단건은 HouseworkInfo가 존재하지 않는다.
                .groupInfo(groupInfo)
                .houseworkMember(gA.toString())
                .category(inWork.getName())
                .title("title")
                .detail("detail")
                .status(HouseworkTodoStatus.HOUSEWORK_TODO_INCOMPLETE)
                .date(LocalDate.now())
                .endTime(LocalTime.now())
                .build());

        // TodoList 생성
        List<Todo> todos = new ArrayList<>();
        todos.add(Todo.builder().id(saveTodo.getId()).build());
        TodoList todoList = todoListRepository.save(generateTodoList(groupInfo.getId(), LocalDate.now(), todos));
        System.out.println("todoList = " + todoList);

        HouseworkInfoDeleteRequest request = HouseworkInfoDeleteRequest.builder()
                .groupId(groupInfo.getId())
                .deleteType(any(HouseworkInfoDeleteType.class))
                .todoId(saveTodo.getId())
                .date(LocalDate.now())
                .build();

        /*
            when
        */
        houseworkInfoService.deleteHouseworkInfo(request);

        /*
            then
        */
        // TODO의 Status가 DELETE로 변경되어야 한다.
        assertThat(houseworkTodoRepository.findById(saveTodo.getId()).get().getStatus()).isEqualTo(HouseworkTodoStatus.HOUSEWORK_TODO_DELETE);

        // 요청 날짜의 todoList의 dirtyBit이 true이어야 한다.
        TodoList findTodoList = todoListRepository.findByGroupInfoIdAndDate(groupInfo.getId(), saveTodo.getDate()).get();
        System.out.println(findTodoList.isDirtyBit());
    }

    @Test
    @DisplayName("비단건 집안일이지만 해당 건만 삭제하고 싶을 경우, " +
            "해당 HouseworkTodo의 status가 DELETE로 변경되고 요청 날짜 TodoList의 dirtyBit이 true가 된다.")
    void deleteHouseworkInfoWhenDeleteOne() {
        /*
            given
        */
        // User 2명 생성 - A, B
        User A = userRepository.save(generateCustomUser("aaa@naver.com", "010-0000-0000"));
        User B = userRepository.save(generateCustomUser("bbb@naver.com", "010-1111-1111"));

        // 그룹 생성 - A가 그룹장
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(A));
        // 그룹 멤버 생성 - A, B
        GroupMember gA = groupMemberRepository.save(generateGroupMember(A, groupInfo));
        GroupMember gB = groupMemberRepository.save(generateGroupMember(B, groupInfo));

        // 카테고리 2개 생성 - 집안일, 집 밖의 일
        HouseworkCategory inWork = houseworkCategoryRepository.save(generateCustomHouseworkCategory("집안일"));
        HouseworkCategory outWork = houseworkCategoryRepository.save(generateCustomHouseworkCategory("집 밖의 일"));

        // 집안일 2개 생성 - 청소기, 빨래
        HouseworkInfo vacuum = houseworkInfoRepository.save(generateCustom2HouseworkInfo(inWork, groupInfo, "청소기 돌리기", HouseworkPeriodType.HOUSEWORK_PERIOD_WEEK));
        HouseworkInfo wash = houseworkInfoRepository.save(generateCustom2HouseworkInfo(outWork, groupInfo, "쓰레기 버리기", HouseworkPeriodType.HOUSEWORK_PERIOD_WEEK));

        // 집안일 담당자 지정 - 청소기: gA, gB / 빨래: gB
        HouseworkMember hA = houseworkMemberRepository.save(generateHouseworkMember(gA, vacuum));
        HouseworkMember hB = houseworkMemberRepository.save(generateHouseworkMember(gB, vacuum));

        // Today 집안일 생성
        HouseworkTodo todayVacuum = houseworkTodoRepository.save(generateHouseworkTodo(vacuum, groupInfo, LocalDate.now()));
        HouseworkTodo todayWash = houseworkTodoRepository.save(generateHouseworkTodo(wash, groupInfo, LocalDate.now()));

        // TodoList 생성
        List<Todo> todos = new ArrayList<>();
        todos.add(Todo.builder().id(todayVacuum.getId()).build());
        todos.add(Todo.builder().id(todayWash.getId()).build());
        TodoList todoList = todoListRepository.save(generateTodoList(groupInfo.getId(), LocalDate.now(), todos));
        System.out.println("todoList = " + todoList);

        HouseworkInfoDeleteRequest request = HouseworkInfoDeleteRequest.builder()
                .groupId(groupInfo.getId())
                .deleteType(HouseworkInfoDeleteType.ONE)
                .todoId(todayVacuum.getId())
                .date(LocalDate.now())
                .build();

        /*
            when
        */
        houseworkInfoService.deleteHouseworkInfo(request);

        /*
            then
        */
        // TODO의 Status가 DELETE로 변경되어야 한다.
        assertThat(houseworkTodoRepository.findById(todayVacuum.getId()).get().getStatus()).isEqualTo(HouseworkTodoStatus.HOUSEWORK_TODO_DELETE);

        // 요청 날짜의 todoList의 dirtyBit이 true이어야 한다.
        TodoList findTodoList = todoListRepository.findByGroupInfoIdAndDate(groupInfo.getId(), todayVacuum.getDate()).get();
        System.out.println(findTodoList.isDirtyBit());
    }

    @Test
    @DisplayName("비단건 집안일이고 반복되는 모든 건 삭제하고 싶을 경우, " +
            "담장자(HOUSEWORK_MEMBER)들의 정보가 삭제되고, " +
            "해당 집안일로 생성된 HOUSEWORK_TODO의 status가 DELETE로 변경된 후 해당 집안일이 삭제된다.")
    void deleteHouseworkInfoWhenNotPeriodDay() {
        /*
            given
        */
        // User 2명 생성 - A, B
        User A = userRepository.save(generateCustomUser("aaa@naver.com", "010-0000-0000"));
        User B = userRepository.save(generateCustomUser("bbb@naver.com", "010-1111-1111"));

        // 그룹 생성 - A가 그룹장
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(A));
        // 그룹 멤버 생성 - A, B
        GroupMember gA = groupMemberRepository.save(generateGroupMember(A, groupInfo));
        GroupMember gB = groupMemberRepository.save(generateGroupMember(B, groupInfo));

        // 카테고리 2개 생성 - 집안일, 집 밖의 일
        HouseworkCategory inWork = houseworkCategoryRepository.save(generateCustomHouseworkCategory("집안일"));
        HouseworkCategory outWork = houseworkCategoryRepository.save(generateCustomHouseworkCategory("집 밖의 일"));

        // 집안일 2개 생성 - 청소기, 빨래
        HouseworkInfo vacuum = houseworkInfoRepository.save(generateCustom2HouseworkInfo(inWork, groupInfo, "청소기 돌리기", HouseworkPeriodType.HOUSEWORK_PERIOD_WEEK));
        HouseworkInfo wash = houseworkInfoRepository.save(generateCustom2HouseworkInfo(outWork, groupInfo, "쓰레기 버리기", HouseworkPeriodType.HOUSEWORK_PERIOD_WEEK));

        // 집안일 담당자 지정 - 청소기: gA, gB / 빨래: gB
        HouseworkMember hA = houseworkMemberRepository.save(generateHouseworkMember(gA, vacuum));
        HouseworkMember hB = houseworkMemberRepository.save(generateHouseworkMember(gB, vacuum));

        // Today 집안일 생성
        HouseworkTodo todayVacuum = houseworkTodoRepository.save(generateHouseworkTodo(vacuum, groupInfo, LocalDate.now()));
        HouseworkTodo todayWash = houseworkTodoRepository.save(generateHouseworkTodo(wash, groupInfo, LocalDate.now()));

        // TodoList 생성
        List<Todo> todos = new ArrayList<>();
        todos.add(Todo.builder().id(todayVacuum.getId()).build());
        todos.add(Todo.builder().id(todayWash.getId()).build());
        TodoList todoList = todoListRepository.save(generateTodoList(groupInfo.getId(), LocalDate.now(), todos));

        HouseworkInfoDeleteRequest request = HouseworkInfoDeleteRequest.builder()
                .groupId(groupInfo.getId())
                .deleteType(HouseworkInfoDeleteType.ALL)
                .todoId(todayVacuum.getId())
                .date(LocalDate.now())
                .build();

        /*
            when
        */
        houseworkInfoService.deleteHouseworkInfo(request);

        /*
            then
        */
        // HOUSEWORK_MEMBER가 조회되지 않아야 한다.
        assertThat(houseworkMemberRepository.findById(hA.getId()).isEmpty());

        // 요청 날짜 이후의 HOUSEWORKTODO는 DELETE 상태여야 한다.
        List<HouseworkTodo> findTodos = houseworkTodoRepository.findHouseworkTodoByHouseworkInfo(hA.getId()).stream()
                .filter(todo -> todo.getDate().isAfter(request.getDate()))
                .collect(Collectors.toList());
        assertAll(() -> findTodos.forEach(todo -> assertEquals("DELETE", todo.getStatus())));

        // 요청 날짜 이후의 todoList는 dirtyBit이 true여야 한다.
        List<TodoList> findTodoLists = todoListRepository.findByGroupInfoId(groupInfo.getId()).stream()
                .filter(td -> // PERIOD에 맞는 TodoList 선별
                        switch (todayVacuum.getHouseworkInfo().getType()) {
                            case HOUSEWORK_PERIOD_DAY -> false;
                            case HOUSEWORK_PERIOD_EVERYDAY -> true;
                            case HOUSEWORK_PERIOD_WEEK ->
                                    DayOfWeekUtils.equals(todayVacuum.getHouseworkInfo().getWeekDate(), td.getDate());
                            case HOUSEWORK_PERIOD_MONTH ->
                                    Arrays.stream(todayVacuum.getHouseworkInfo().getMonthDate().split(","))
                                            .anyMatch(d -> Integer.parseInt(d) == td.getDate().getDayOfMonth());
                        })
                .collect(Collectors.toList());
        assertAll(() -> findTodoLists.forEach(list -> assertTrue(list.isDirtyBit())));

        // 집안일이 조회되지 않아야 한다.
        assertThat(houseworkInfoRepository.findById(vacuum.getId())).isEmpty();
    }

    @Test
    @DisplayName("수정 페이지에 내려줄 정보를 잘 불러온다. - 단건 집안일의 경우")
    void updateHouseworkWithPeriodDay() {
        /*
            given
        */
        User A = userRepository.save(generateCustomUser("aaa@naver.com", "010-0000-0000"));
        User B = userRepository.save(generateCustomUser("bbb@naver.com", "010-1111-1111"));
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(A));
        GroupMember gA = groupMemberRepository.save(generateGroupMember(A, groupInfo));
        GroupMember gB = groupMemberRepository.save(generateGroupMember(B, groupInfo));
        HouseworkCategory inWork = houseworkCategoryRepository.save(generateCustomHouseworkCategory("집안일"));
        HouseworkTodo todo = houseworkTodoRepository.save(HouseworkTodo.builder()
                .houseworkInfo(null) // 단건은 HouseworkInfo가 존재하지 않는다.
                .groupInfo(groupInfo)
                .houseworkMember(gA.getId() + "," + gB.getId())
                .category(inWork.getName())
                .title("title")
                .detail("detail")
                .status(HouseworkTodoStatus.HOUSEWORK_TODO_INCOMPLETE)
                .date(LocalDate.of(2023, 11, 18))
                .endTime(LocalTime.of(16, 30))
                .build());


        // when
        HouseworkInfoUpdatePageResponse response = houseworkInfoService.updateHouseworkPage(todo.getId());

        // then
        assertThat(response.getTitle()).isEqualTo("title");
        assertThat(response.getHouseworkCategoryId()).isEqualTo(inWork.getId());
        assertThat(response.getType()).isEqualTo(HouseworkPeriodType.HOUSEWORK_PERIOD_DAY);
        assertThat(response.getLocalDate()).isEqualTo(todo.getDate());
        assertThat(response.getWeekDate()).isNull();
        assertThat(response.getMonthDate()).isNull();
        assertThat(response.getEndTime()).isEqualTo(todo.getEndTime());
    }

    @Test
    @DisplayName("수정 페이지에 내려줄 정보를 잘 불러온다. - 비단건 집안일의 경우")
    void updateHouseworkWithPeriodNotDay() {
        /*
            given
        */
        User A = userRepository.save(generateCustomUser("aaa@naver.com", "010-0000-0000"));
        User B = userRepository.save(generateCustomUser("bbb@naver.com", "010-1111-1111"));
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(A));
        GroupMember gA = groupMemberRepository.save(generateGroupMember(A, groupInfo));
        GroupMember gB = groupMemberRepository.save(generateGroupMember(B, groupInfo));
        HouseworkCategory inWork = houseworkCategoryRepository.save(generateCustomHouseworkCategory("집안일"));
        HouseworkInfo h = houseworkInfoRepository.save(generateCustom2HouseworkInfo(inWork, groupInfo, "test", HouseworkPeriodType.HOUSEWORK_PERIOD_WEEK));
        HouseworkTodo todo = houseworkTodoRepository.save(HouseworkTodo.builder()
                .houseworkInfo(h)
                .groupInfo(groupInfo)
                .houseworkMember(gA.getId() + "," + gB.getId())
                .category(h.getHouseworkCategory().getName())
                .title(h.getTitle())
                .detail(h.getDetail())
                .status(HouseworkTodoStatus.HOUSEWORK_TODO_INCOMPLETE)
                .date(LocalDate.now())
                .endTime(LocalTime.of(16, 30))
                .build());


        // when
        HouseworkInfoUpdatePageResponse response = houseworkInfoService.updateHouseworkPage(todo.getId());

        // then
        assertThat(response.getTitle()).isEqualTo("test");
        assertThat(response.getHouseworkCategoryId()).isEqualTo(h.getHouseworkCategory().getId());
        assertThat(response.getType()).isEqualTo(h.getType());
        assertThat(response.getLocalDate()).isEqualTo(h.getDayDate());
        assertThat(response.getEndTime()).isEqualTo(h.getEndTime());
    }

    @Test
    @DisplayName("단건 집안일을 단건 집안일로 변경했을 경우 HouseworkTodo가 수정된다.")
    void updateHouseworkWithDayToDay() {
        // given
        // 단건 집안일
        User A = userRepository.save(generateCustomUser("aaa@naver.com", "010-0000-0000"));
        User B = userRepository.save(generateCustomUser("bbb@naver.com", "010-1111-1111"));
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(A));
        GroupMember gA = groupMemberRepository.save(generateGroupMember(A, groupInfo));
        GroupMember gB = groupMemberRepository.save(generateGroupMember(B, groupInfo));
        HouseworkCategory inWork = houseworkCategoryRepository.save(generateCustomHouseworkCategory("집안일"));
        HouseworkCategory outWork = houseworkCategoryRepository.save(generateCustomHouseworkCategory("집 밖의 일"));
        HouseworkTodo todo = houseworkTodoRepository.save(HouseworkTodo.builder()
                .houseworkInfo(null) // 단건은 HouseworkInfo가 존재하지 않는다.
                .groupInfo(groupInfo)
                .houseworkMember(gA.getId() + "," + gB.getId())
                .category(inWork.getName())
                .title("title")
                .detail("detail")
                .status(HouseworkTodoStatus.HOUSEWORK_TODO_INCOMPLETE)
                .date(LocalDate.of(2023, 11, 18))
                .endTime(LocalTime.of(16, 30))
                .build());
        List<Todo> todos = new ArrayList<>();
        todos.add(Todo.builder().id(todo.getId()).build());
        TodoList todoList = todoListRepository.save(generateTodoList(groupInfo.getId(), LocalDate.of(2023, 11, 18), todos));

        // 수정 요청 DTO
        HouseworkInfoUpdateServiceRequest request = HouseworkInfoUpdateServiceRequest.builder()
                .groupMemberIdList(Arrays.asList(gA.getId()))       // 담당자 변경
                .houseworkCategoryId(outWork.getId())               // 카테고리 변경
                .title("updateTitle")                               // 제목 변경
                .detail("updateDetail")                             // detail 변경
                .type(HouseworkPeriodType.HOUSEWORK_PERIOD_DAY)     // 단건 -> 단건
                .dayDate(LocalDate.of(2023, 11, 19))    // 변경
                .endTime(LocalTime.of(18, 30))          // 마감 시간 변경
                .groupId(groupInfo.getId())
                .todoId(todo.getId())
                .requestDate(LocalDate.of(2023, 11, 18))
                .build();

        // when
        houseworkInfoService.updateHousework(request);


    }

    @Test
    @DisplayName("단건 집안일을 비단건 집안일로 변경했을 경우 HouseworkTodo가 삭제되고 HouseworkInfo가 생성된다.")
    void updateHouseworkWithDayToNotDay() {

    }

    @Test
    @DisplayName("비단건 집안일을 단건 집안일로 변경했을 경우 HouseworkInfo가 삭제되고 HouseworkTodo가 생성된다.")
    void updateHouseworkWithNotDayToDay() {

    }

    @Test
    @DisplayName("비단건 집안일을 비단건 집안일로 변경했을 경우 이전의 HouseworkTodo들이 삭제되고 HouseworkInfo가 수정된다.")
    void updateHouseworkWithNotDayToNotDay() {

    }
}