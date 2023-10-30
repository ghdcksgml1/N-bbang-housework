package com.heachi.redis.define.housework.todo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
@RedisHash(value = "todoList", timeToLive = 60 * 60 * 24) // 1일 동안 캐싱
public class TodoList {
    @Id
    private String id;

    @Indexed
    private Long groupInfoId;
    @Indexed
    private LocalDate date;
    private List<Todo> todoList;
    private boolean dirtyBit = false;

    @Builder
    private TodoList(Long groupInfoId, LocalDate date, List<Todo> todoList) {
        this.groupInfoId = groupInfoId;
        this.date = date;
        this.todoList = todoList;
    }

    public void checkDirtyBit() {
        this.dirtyBit = true;
    }
}
