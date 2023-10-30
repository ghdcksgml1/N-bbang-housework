package com.heachi.mysql.define.housework.todo.repository;

import com.heachi.mysql.define.housework.todo.repository.response.HouseworkTodoCount;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.heachi.mysql.define.group.info.QGroupInfo.groupInfo;
import static com.heachi.mysql.define.housework.todo.QHouseworkTodo.houseworkTodo;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Component
@RequiredArgsConstructor
public class HouseworkTodoRepositoryImpl implements HouseworkTodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<HouseworkTodoCount> findHouseworkTodoCountByGroupInfoIdList(List<Long> groupInfoIdList) {

        return queryFactory
                .from(houseworkTodo)
                .where(houseworkTodo.groupInfo.id.in(groupInfoIdList)
                        .and(houseworkTodo.date.eq(LocalDate.now()))) // 오늘
                .transform(
                        groupBy(houseworkTodo.groupInfo.id).list(
                                Projections.fields(
                                        HouseworkTodoCount.class,
                                        houseworkTodo.groupInfo.id.as("id"),
                                        list(houseworkTodo.status).as("status")
                                )
                        )
                );
    }
}
