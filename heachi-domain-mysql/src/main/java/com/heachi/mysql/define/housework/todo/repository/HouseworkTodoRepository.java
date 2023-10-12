package com.heachi.mysql.define.housework.todo.repository;

import com.heachi.mysql.define.housework.todo.HouseworkTodo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseworkTodoRepository extends JpaRepository<HouseworkTodo, Long> {
}
