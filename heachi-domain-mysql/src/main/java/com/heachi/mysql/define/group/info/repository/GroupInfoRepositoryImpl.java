package com.heachi.mysql.define.group.info.repository;

import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.info.repository.response.GroupInfoGroupMember;
import com.heachi.mysql.define.group.info.repository.response.GroupInfoUserGroupResponse;
import com.heachi.mysql.define.group.member.constant.GroupMemberStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.heachi.mysql.define.group.info.QGroupInfo.groupInfo;
import static com.heachi.mysql.define.group.member.QGroupMember.groupMember;
import static com.heachi.mysql.define.user.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Component
@RequiredArgsConstructor
public class GroupInfoRepositoryImpl implements GroupInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GroupInfoUserGroupResponse> findGroupInfoUserGroupResponseListByUserEmail(String email) {

        List<GroupInfo> groupInfoList = queryFactory
                .selectFrom(groupInfo)
                .leftJoin(groupInfo.groupMembers, groupMember).fetchJoin()
                .innerJoin(groupMember.user, user).fetchJoin()
                .where(groupInfo.id.in(
                        JPAExpressions // 유저가 속해있는 그룹 아이디 가져오기
                                .select(groupMember.groupInfo.id)
                                .from(groupMember)
                                .innerJoin(groupMember.user, user)
                                .where(user.email.eq(email)
                                        .and(groupMember.status.eq(GroupMemberStatus.ACCEPT)))
                ))
                .fetch();

        return groupInfoList.stream()
                .map(groupInfo -> GroupInfoUserGroupResponse.builder()
                        .id(groupInfo.getId())
                        .name(groupInfo.getName())
                        .groupMembers(
                                groupInfo.getGroupMembers().stream()
                                        // 현재 그룹 멤버만
                                        .filter(groupMember -> groupMember.getStatus() == GroupMemberStatus.ACCEPT)
                                        .map(groupMember -> GroupInfoGroupMember.builder()
                                                .name(groupMember.getUser().getName())
                                                .profileImageUrl(groupMember.getUser().getProfileImageUrl())
                                                .build())
                                        .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GroupInfo> findGroupInfoByGroupIdJoinFetchUser(Long groupId) {
        return Optional.ofNullable(queryFactory.selectFrom(groupInfo)
                .innerJoin(groupInfo.user, user).fetchJoin()
                .where(groupInfo.id.eq(groupId))
                .fetchOne());

    }
}
