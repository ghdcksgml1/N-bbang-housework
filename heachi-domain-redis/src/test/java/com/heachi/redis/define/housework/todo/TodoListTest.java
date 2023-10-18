package com.heachi.redis.define.housework.todo;

import com.heachi.redis.define.housework.todo.repository.TodoListRepository;
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
class TodoListTest {

    @Autowired
    private TodoListRepository todoListRepository;

    @Test
    @DisplayName("TodoList를 생성해서 Redis에 저장할때 객체들이 잘 저장되는지 확인하는 테스트")
    void todoListSaveTest() {
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
        TodoList result = todoListRepository.findById(todoList.getId()).get();

        // then
        assertThat(result).isNotNull();
    }

}