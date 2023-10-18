package com.heachi.mysql.define.housework.info.repository;

import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.info.QHouseworkInfo;
import com.heachi.mysql.define.housework.member.QHouseworkMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.heachi.mysql.define.group.info.QGroupInfo.groupInfo;
import static com.heachi.mysql.define.housework.category.QHouseworkCategory.houseworkCategory;
import static com.heachi.mysql.define.housework.info.QHouseworkInfo.*;
import static com.heachi.mysql.define.housework.member.QHouseworkMember.*;

@Component
@RequiredArgsConstructor
public class HouseworkInfoRepositoryImpl implements HouseworkInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<HouseworkInfo> findHouseworkInfoByIdJoinFetchHouseworkMembers(Long houseworkInfoId) {

        return Optional.of(queryFactory.selectFrom(houseworkInfo)
                .leftJoin(houseworkInfo.houseworkMembers, houseworkMember).fetchJoin()
                .innerJoin(houseworkInfo.houseworkCategory, houseworkCategory).fetchJoin()
                .where(houseworkInfo.id.eq(houseworkInfoId))
                .fetchOne());
    }

    @Override
    public List<HouseworkInfo> findHouseworkInfoByGroupInfoId(Long groupId) {

        return queryFactory.selectFrom(houseworkInfo)
                .leftJoin(houseworkInfo.houseworkMembers, houseworkMember).fetchJoin()
                .innerJoin(houseworkInfo.houseworkCategory, houseworkCategory).fetchJoin()
                .innerJoin(houseworkInfo.groupInfo, groupInfo).fetchJoin()
                .where(houseworkInfo.groupInfo.id.eq(groupId))
                .fetch();
    }

}
