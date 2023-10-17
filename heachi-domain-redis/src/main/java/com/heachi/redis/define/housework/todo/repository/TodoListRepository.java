package com.heachi.redis.define.housework.todo.repository;

import com.heachi.redis.define.housework.todo.TodoList;
import org.springframework.data.repository.CrudRepository;

public interface TodoListRepository extends CrudRepository<TodoList, String> {
}
