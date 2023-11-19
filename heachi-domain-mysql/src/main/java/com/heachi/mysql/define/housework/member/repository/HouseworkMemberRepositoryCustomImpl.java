package com.heachi.mysql.define.housework.member.repository;

import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.heachi.mysql.define.housework.member.QHouseworkMember.houseworkMember;

@Component
@RequiredArgsConstructor
public class HouseworkMemberRepositoryCustomImpl implements HouseworkMemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    @Override
    public boolean deleteHouseworkMemberIfGroupMemberIdIn(HouseworkInfo info, List<Long> groupIdList) {
        List<HouseworkMember> houseworkMemberList = queryFactory.selectFrom(houseworkMember)
                .where(houseworkMember.houseworkInfo.eq(info)).fetch();
        List<Long> houseworkGroupMemberIdList = houseworkMemberList.stream()
                .map(hm -> hm.getGroupMember().getId())
                .collect(Collectors.toList());

        // houseworkGroupMemberIdList의 요소들이 groupIdList의 요소들과 정확히 일치하면 true 리턴
        return new HashSet<>(groupIdList).containsAll(houseworkGroupMemberIdList);
    }
}
