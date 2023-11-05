package com.heachi.mysql.define.group.member.repository;

import com.heachi.mysql.define.group.info.QGroupInfo;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.group.member.constant.GroupMemberStatus;
import com.heachi.mysql.define.user.User;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.heachi.mysql.define.group.info.QGroupInfo.*;
import static com.heachi.mysql.define.group.member.QGroupMember.groupMember;
import static com.heachi.mysql.define.housework.todo.QHouseworkTodo.houseworkTodo;
import static com.heachi.mysql.define.user.QUser.user;

@Component
@RequiredArgsConstructor
public class GroupMemberRepositoryImpl implements GroupMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsGroupMemberByUserEmailAndGroupInfoId(String email, Long groupId) {

        return queryFactory.from(groupMember)
                .where(groupMember.groupInfo.id.eq(groupId)
                        .and(groupMember.user.id.eq(
                                // Sub Query
                                JPAExpressions
                                        .select(user.id)
                                        .from(user)
                                        .where(user.email.eq(email))))
                        .and(groupMember.status.eq(GroupMemberStatus.ACCEPT)))
                .fetchFirst() != null;
    }

    @Override
    public List<GroupMember> findGroupMemberByGroupId(Long groupId) {

        return queryFactory.selectFrom(groupMember)
                .innerJoin(groupMember.user, user).fetchJoin()
                .where(groupMember.groupInfo.id.eq(groupId)
                        .and(groupMember.status.eq(GroupMemberStatus.ACCEPT)))
                .fetch();
    }

    @Override
    public List<GroupMember> findGroupMemberListByGroupMemberIdList(List<Long> groupMemberIdList) {

        // select gm from groupMember gm where gm.id in groupMemberIdList and gm.status = 'ACCEPT'
        return queryFactory.selectFrom(groupMember)
                .innerJoin(groupMember.groupInfo, groupInfo).fetchJoin()
                .where(groupMember.id.in(groupMemberIdList)
                        .and(groupMember.status.eq(GroupMemberStatus.ACCEPT)))
                .fetch();
    }

    @Override
    public Optional<GroupMember> findGroupMemberByUserEmailAndTodoId(String email, Long todoId) {

        return Optional.ofNullable(queryFactory
                .selectFrom(groupMember)
                .innerJoin(groupMember.user, user)
                .where(groupMember.groupInfo.id.eq(
                                JPAExpressions.select(groupInfo.id)
                                        .from(houseworkTodo)
                                        .innerJoin(houseworkTodo.groupInfo, groupInfo)
                                        .where(houseworkTodo.id.eq(todoId)))
                        .and(user.email.eq(email)))
                .fetchOne());
    }
                                   
    @Override
    public Optional<GroupMember> findGroupMemberByGroupMemberIdAndGroupInfoId(Long groupMemberId, Long groupId) {
        // select gm from groupMember gm where gm.id= :groupMemberId and gm.groupInfo.id= :groupId
        return Optional.ofNullable(queryFactory.selectFrom(groupMember)
                .innerJoin(groupMember.groupInfo, groupInfo).fetchJoin()
                .where(groupMember.id.eq(groupMemberId)
                        .and(groupMember.groupInfo.id.eq(groupId)))
                .fetchOne());
    }

    @Override
    public Optional<GroupMember> findGroupMemberByUserEmailAndGroupInfoId(String userEmail, Long groupId) {
        return Optional.ofNullable(queryFactory.selectFrom(groupMember)
                .innerJoin(groupMember.user, user).fetchJoin()
                .innerJoin(groupMember.groupInfo, groupInfo).fetchJoin()
                .where(groupMember.user.email.eq(userEmail)
                        .and(groupMember.groupInfo.id.eq(groupId)))
                .fetchOne());
    }
}
