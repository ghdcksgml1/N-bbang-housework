package com.heachi.mysql.define.housework.todo.repository;

import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.todo.HouseworkTodo;
import com.heachi.mysql.define.housework.todo.constant.HouseworkTodoStatus;
import com.heachi.mysql.define.housework.todo.repository.response.HouseworkTodoCount;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.heachi.mysql.define.group.info.QGroupInfo.groupInfo;
import static com.heachi.mysql.define.group.member.QGroupMember.groupMember;
import static com.heachi.mysql.define.housework.category.QHouseworkCategory.houseworkCategory;
import static com.heachi.mysql.define.housework.info.QHouseworkInfo.houseworkInfo;
import static com.heachi.mysql.define.housework.member.QHouseworkMember.houseworkMember;
import static com.heachi.mysql.define.housework.todo.QHouseworkTodo.houseworkTodo;
import static com.heachi.mysql.define.user.QUser.user;
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

    @Override
    public Optional<HouseworkTodo> findHouseworkTodoByIdAndGroupMemberId(Long todoId, Long groupMemberId) {
        HouseworkTodo findHouseworkTodo = queryFactory
                .selectFrom(houseworkTodo)
                .where(houseworkTodo.id.eq(todoId))
                .fetchOne();

        if (findHouseworkTodo != null && !findHouseworkTodo.getHouseworkMember().isEmpty()) {
            return Arrays.stream(findHouseworkTodo.getHouseworkMember().split(","))
                    .map(Long::parseLong)
                    .anyMatch(gmId -> gmId.equals(groupMemberId)) ? Optional.of(findHouseworkTodo) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public long updateHouseworkTodoByHouseworkInfoId(Long houseworkInfoId) {

        return queryFactory
                .update(houseworkTodo)
                .setNull(houseworkTodo.houseworkInfo)
                .where(houseworkTodo.houseworkInfo.id.eq(houseworkInfoId))
                .execute();
    }


    @Override
    public Optional<HouseworkTodo> findHouseworkTodoByIdJoinFetchHouseworkInfo(Long todoId) {

        return Optional.ofNullable(queryFactory.selectFrom(houseworkTodo)
                .leftJoin(houseworkTodo.houseworkInfo, houseworkInfo).fetchJoin()
                .where(houseworkTodo.id.eq(todoId))
                .fetchOne());
    }

    @Override
    public List<HouseworkTodo> findHouseworkTodoByHouseworkInfo(Long houseworkInfoId) {
        return queryFactory
                .selectFrom(houseworkTodo)
                .where(houseworkTodo.houseworkInfo.id.eq(houseworkInfoId))
                .fetch();
    }
}
