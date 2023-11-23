package com.heachi.mysql.define.housework.member.repository;

import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.heachi.mysql.define.housework.member.QHouseworkMember.houseworkMember;

@Component
@RequiredArgsConstructor
public class HouseworkMemberRepositoryCustomImpl implements HouseworkMemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Transactional
    @Override
    public void deleteByHouseworkInfoList(List<HouseworkInfo> houseworkInfoList) {
        queryFactory.delete(houseworkMember)
                .where(houseworkMember.houseworkInfo.in(houseworkInfoList))
                .execute();
    }
}
