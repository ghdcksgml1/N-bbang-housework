package com.heachi.redis.define.housework.todo.repository;

import com.heachi.redis.define.housework.todo.TodoList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface TodoListRepository extends CrudRepository<TodoList, String> {

    public TodoList findByGroupInfoIdAndDate(@Param("groupInfoId") Long groupInfoId, @Param("date") LocalDate date);

}
