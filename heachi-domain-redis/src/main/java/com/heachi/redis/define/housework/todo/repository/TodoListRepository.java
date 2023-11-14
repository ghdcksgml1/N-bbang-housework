package com.heachi.redis.define.housework.todo.repository;

import com.heachi.redis.define.housework.todo.Todo;
import com.heachi.redis.define.housework.todo.TodoList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoListRepository extends CrudRepository<TodoList, String> {

    List<TodoList> findByGroupInfoId(@Param("groupInfoId") Long groupInfoId);

    Optional<TodoList> findByGroupInfoIdAndDate(@Param("groupInfoId") Long groupInfoId, @Param("date") LocalDate date);

    // todoList.id가 todoIdList에 있는 값들 중에 하나라도 일치하는 TodoList들을 조회한다.
    List<TodoList> findByTodoList_IdIn(@Param("todoIdList") List<Long> todoIdList);

}
