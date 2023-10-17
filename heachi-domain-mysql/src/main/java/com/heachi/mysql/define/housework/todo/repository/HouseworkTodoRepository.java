package com.heachi.mysql.define.housework.todo.repository;

import com.heachi.mysql.define.housework.todo.HouseworkTodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HouseworkTodoRepository extends JpaRepository<HouseworkTodo, Long> {

    @Query("select ht from HOUSEWORK_TODO ht where ht.groupInfo.id = :groupId and ht.date = :date")
    public List<HouseworkTodo> findByGroupInfoAndDate(@Param(value = "groupId") Long groupId,
                                                     @Param(value = "date") LocalDate date);

}
