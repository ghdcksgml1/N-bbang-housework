package com.heachi.housework.api.service.housework.todo;

import com.heachi.housework.TestConfig;
import com.heachi.housework.api.service.housework.todo.request.TodoSelectRequest;
import com.heachi.housework.api.service.housework.todo.request.VerifyTodoServiceRequest;
import com.heachi.housework.api.service.housework.todo.response.TodoResponse;
import com.heachi.housework.api.service.housework.todo.response.TodoUser;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.GroupInfoRepository;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.category.repository.HouseworkCategoryRepository;
import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.info.repository.HouseworkInfoRepository;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import com.heachi.mysql.define.housework.member.repository.HouseworkMemberRepository;
import com.heachi.mysql.define.housework.todo.HouseworkTodo;
import com.heachi.mysql.define.housework.todo.constant.HouseworkTodoStatus;
import com.heachi.mysql.define.housework.todo.repository.HouseworkTodoRepository;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.repository.UserRepository;
import com.heachi.redis.define.housework.todo.TodoList;
import com.heachi.redis.define.housework.todo.repository.TodoListRepository;
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
class TodoServiceTest extends TestConfig {
    @Autowired private GroupMemberRepository groupMemberRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private GroupInfoRepository groupInfoRepository;
    @Autowired private HouseworkCategoryRepository houseworkCategoryRepository;
    @Autowired private HouseworkInfoRepository houseworkInfoRepository;
    @Autowired private HouseworkTodoRepository houseworkTodoRepository;
    @Autowired private HouseworkMemberRepository houseworkMemberRepository;

    @Autowired private TodoService todoService;
    @Autowired private TodoListRepository todoListRepository;

    @AfterEach
    void tearDown() {
        houseworkTodoRepository.deleteAllInBatch();
        houseworkMemberRepository.deleteAllInBatch();
        houseworkInfoRepository.deleteAllInBatch();
        houseworkCategoryRepository.deleteAllInBatch();
        groupMemberRepository.deleteAllInBatch();
        groupInfoRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        todoListRepository.deleteAll();
    }

    @Test
    @DisplayName("INFO에 저장되어 있지만, 반영되지 않은 TODO는 생성된다.")
    void test1() {
        // given
        User user = userRepository.save(generateUser());
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(user));
        GroupMember groupMember = groupMemberRepository.save(generateGroupMember(user, groupInfo));

        HouseworkCategory houseworkCategory = houseworkCategoryRepository.save(generateHouseworkCategory());
        HouseworkInfo houseworkInfo = houseworkInfoRepository.save(generateHouseworkInfo(groupInfo, houseworkCategory));

        HouseworkMember houseworkMember = houseworkMemberRepository.save(generateHouseworkMember(groupMember, houseworkInfo));
        HouseworkInfo findHouseworkInfo = houseworkInfoRepository.findHouseworkInfoByIdJoinFetchHouseworkMembers(houseworkInfo.getId()).get();

        // when
        List<TodoResponse> todoResponses = todoService.selectTodo(TodoSelectRequest.builder().groupId(groupInfo.getId()).date(LocalDate.now()).build());

        // then
        assertAll(() -> {
            TodoResponse todoResponse = todoResponses.get(0);
            TodoUser todoUser = todoResponse.getHouseworkMembers().get(0);
            assertThat(todoUser.getName()).isEqualTo("kms");
            assertThat(todoUser.getEmail()).isEqualTo("kms@kakao.com");
            assertThat(todoUser.getProfileImageUrl()).isEqualTo("https://google.com");

            assertThat(todoResponse.getCategory()).isEqualTo("집안일");
            assertThat(todoResponse.getTitle()).isEqualTo("빨래");
            assertThat(todoResponse.getDetail()).isEqualTo("빨래 돌리기");
            assertThat(todoResponse.getStatus()).isEqualTo(HouseworkTodoStatus.HOUSEWORK_TODO_INCOMPLETE);
            assertThat(todoResponse.getTitle()).isEqualTo("빨래");
        });
    }

    @Test
    @DisplayName("INFO에 저장되어 있으면서, 반영되어 있는 TODO는 생성되지 않는다.")
    void test2() {
        // given
        User user = userRepository.save(generateUser());
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(user));
        GroupMember groupMember = groupMemberRepository.save(generateGroupMember(user, groupInfo));

        HouseworkCategory houseworkCategory = houseworkCategoryRepository.save(generateHouseworkCategory());
        HouseworkInfo houseworkInfo = houseworkInfoRepository.save(generateHouseworkInfo(groupInfo, houseworkCategory));

        HouseworkMember houseworkMember = houseworkMemberRepository.save(generateHouseworkMember(groupMember, houseworkInfo));
        HouseworkInfo findHouseworkInfo = houseworkInfoRepository.findHouseworkInfoByIdJoinFetchHouseworkMembers(houseworkInfo.getId()).get();

        houseworkTodoRepository.save(generateHouseworkTodo(findHouseworkInfo, groupInfo, LocalDate.now()));

        // when
        List<TodoResponse> todoResponses = todoService.selectTodo(TodoSelectRequest.builder().groupId(groupInfo.getId()).date(LocalDate.now()).build());

        // then
        assertAll(() -> {
            TodoResponse todoResponse = todoResponses.get(0);
            TodoUser todoUser = todoResponse.getHouseworkMembers().get(0);
            assertThat(todoUser.getName()).isEqualTo("kms");
            assertThat(todoUser.getEmail()).isEqualTo("kms@kakao.com");
            assertThat(todoUser.getProfileImageUrl()).isEqualTo("https://google.com");

            assertThat(todoResponse.getCategory()).isEqualTo("집안일");
            assertThat(todoResponse.getTitle()).isEqualTo("빨래");
            assertThat(todoResponse.getDetail()).isEqualTo("빨래 돌리기");
            assertThat(todoResponse.getStatus()).isEqualTo(HouseworkTodoStatus.HOUSEWORK_TODO_INCOMPLETE);
            assertThat(todoResponse.getTitle()).isEqualTo("빨래");
        });
    }

    @Test
    @DisplayName("캐싱되어 있는 데이터가 있으면 그대로 가져와 리턴한다.")
    void test3() {
        // given
        User user = userRepository.save(generateUser());
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(user));
        GroupMember groupMember = groupMemberRepository.save(generateGroupMember(user, groupInfo));

        HouseworkCategory houseworkCategory = houseworkCategoryRepository.save(generateHouseworkCategory());
        HouseworkInfo houseworkInfo = houseworkInfoRepository.save(generateHouseworkInfo(groupInfo, houseworkCategory));

        HouseworkMember houseworkMember = houseworkMemberRepository.save(generateHouseworkMember(groupMember, houseworkInfo));

        // when
        todoService.cachedSelectTodo(TodoSelectRequest.builder().groupId(groupInfo.getId()).date(LocalDate.now()).build());

        // then
        TodoList todoList = todoService.cachedSelectTodo(TodoSelectRequest.builder().groupId(groupInfo.getId()).date(LocalDate.now()).build());
        assertThat(todoList.getGroupInfoId()).isEqualTo(groupInfo.getId());
        assertThat(todoList.getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("캐싱되어 있는 데이터가 있지만, dirtyBit가 true라면 HOUSEWORK_TODO를 최신화 시켜 캐싱한다.")
    void test4() {
        // given
        User user = userRepository.save(generateUser());
        User user2 = userRepository.save(generateCustomUser("kmm@kakao.com", "010-1111-1111"));
        GroupInfo groupInfo = groupInfoRepository.save(generateGroupInfo(user));
        GroupMember groupMember = groupMemberRepository.save(generateGroupMember(user, groupInfo));
        GroupMember groupMember2 = groupMemberRepository.save(generateGroupMember(user2, groupInfo));

        HouseworkCategory houseworkCategory = houseworkCategoryRepository.save(generateHouseworkCategory());
        HouseworkInfo houseworkInfo = houseworkInfoRepository.save(generateHouseworkInfo(groupInfo, houseworkCategory));

        HouseworkMember houseworkMember = houseworkMemberRepository.save(generateHouseworkMember(groupMember, houseworkInfo));
        HouseworkMember houseworkMember2 = houseworkMemberRepository.save(generateHouseworkMember(groupMember2, houseworkInfo));

        // when
        TodoList todoList = todoService.cachedSelectTodo(TodoSelectRequest.builder().groupId(groupInfo.getId()).date(LocalDate.now()).build());

        // HOUSEWORK_INFO 추가
        HouseworkInfo houseworkInfo1 = houseworkInfoRepository.save(generateHouseworkInfo(groupInfo, houseworkCategory));
        todoList.checkDirtyBit();
        todoListRepository.save(todoList);

        TodoList result = todoService.cachedSelectTodo(TodoSelectRequest.builder().groupId(groupInfo.getId()).date(LocalDate.now()).build());

        // then
        assertThat(result.isDirtyBit()).isFalse();
        assertThat(result.getTodoList().size()).isEqualTo(2);
        System.out.println("result = " + result);
    }

    @Test
    @DisplayName("Todo 담당자가 사진을 업로드하면 정상적으로 사진이 저장된다.")
    void test5() {
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

        String uploadedImageURL = "abcd.sfsa";

        // when
        todoService.verifyTodo(VerifyTodoServiceRequest.builder()
                .verifyImageURL(uploadedImageURL)
                .todoId(houseworkTodo.getId())
                .email(user.getEmail())
                .build());
        HouseworkTodo findHouseworkTodo = houseworkTodoRepository.findById(houseworkTodo.getId()).get();

        // then
        assertThat(findHouseworkTodo.getVerificationPhotoURL()).isEqualTo(uploadedImageURL);
        assertThat(findHouseworkTodo.getVerificationTime()).isNotNull();
    }
}