package com.heachi.mysql.define.group.member.repository;

import com.heachi.mysql.define.group.member.constant.GroupMemberStatus;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
}
