package com.heachi.redis.define.housework.todo.repository;

import com.heachi.redis.define.housework.todo.Todo;
import com.heachi.redis.define.housework.todo.TodoList;
import com.heachi.redis.define.housework.todo.TodoUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TodoListRepositoryTest {
    @Autowired
    private TodoListRepository todoListRepository;

    @AfterEach
    void tearDown() {
        todoListRepository.deleteAll();
    }

    @Test
    @DisplayName("groupId로 TodoList를 조회한다.")
    void test1() {
        // given
        LocalDate date = LocalDate.now();
        LocalTime endTime = LocalTime.of(18, 0);

        TodoList todoList = todoListRepository.save(TodoList.builder()
                .groupInfoId(1L)
                .date(date)
                .todoList(List.of(Todo.builder()
                        .id(1L)
                        .houseworkMembers(List.of(TodoUser.builder()
                                .name("kms")
                                .email("kms@kakao.com")
                                .profileImageUrl("https://google.com")
                                .build()))
                        .category("집안일")
                        .title("빨래")
                        .detail("빨래 돌리기")
                        .status("HOUSEWORK_TODO_INCOMPLETE")
                        .date(date)
                        .endTime(endTime)
                        .build()))
                .build());

        // when
        TodoList findTodoList = todoListRepository.findByGroupInfoId(1L).get(0);

        // then
        assertThat(findTodoList).isNotNull();
        assertThat(findTodoList.getDate()).isEqualTo(date);
    }

    @Test
    @DisplayName("groupId와 date로 TodoList를 조회한다.")
    void test2() {
        // given
        LocalDate date = LocalDate.now();
        LocalTime endTime = LocalTime.of(18, 0);

        TodoList todoList = todoListRepository.save(TodoList.builder()
                .groupInfoId(1L)
                .date(date)
                .todoList(List.of(Todo.builder()
                        .id(1L)
                        .houseworkMembers(List.of(TodoUser.builder()
                                .name("kms")
                                .email("kms@kakao.com")
                                .profileImageUrl("https://google.com")
                                .build()))
                        .category("집안일")
                        .title("빨래")
                        .detail("빨래 돌리기")
                        .status("HOUSEWORK_TODO_INCOMPLETE")
                        .date(date)
                        .endTime(endTime)
                        .build()))
                .build());

        // when
        TodoList findTodoList = todoListRepository.findByGroupInfoIdAndDate(1L, date).get();

        // then
        assertThat(findTodoList).isNotNull();
        assertThat(findTodoList.getDate()).isEqualTo(date);
    }
}