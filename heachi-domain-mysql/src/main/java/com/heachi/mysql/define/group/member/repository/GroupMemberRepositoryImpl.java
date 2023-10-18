package com.heachi.mysql.define.group.member.repository;

import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.group.member.constant.GroupMemberStatus;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.heachi.mysql.define.group.member.QGroupMember.groupMember;
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
        // select gm from groupMember gm where gm.id in groupMemberIdList
        return queryFactory.selectFrom(groupMember)
                .where(groupMember.id.in(groupMemberIdList))
                .fetch();
    }
}
