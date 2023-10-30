package com.heachi.redis.define.housework.todo.repository;

import com.heachi.redis.define.housework.todo.TodoList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoListRepository extends CrudRepository<TodoList, String> {

    List<TodoList> findByGroupInfoId(@Param("groupInfoId") Long groupInfoId);

    Optional<TodoList> findByGroupInfoIdAndDate(@Param("groupInfoId") Long groupInfoId, @Param("date") LocalDate date);

}
