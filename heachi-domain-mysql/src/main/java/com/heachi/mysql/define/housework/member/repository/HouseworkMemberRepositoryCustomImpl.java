package com.heachi.mysql.define.housework.member.repository;

import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.heachi.mysql.define.group.member.QGroupMember.groupMember;
import static com.heachi.mysql.define.housework.member.QHouseworkMember.houseworkMember;

@Component
@RequiredArgsConstructor
public class HouseworkMemberRepositoryCustomImpl implements HouseworkMemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final HouseworkMemberRepository houseworkMemberRepository;


    @Override
    public boolean deleteHouseworkMemberIfGroupMemberIdIn(HouseworkInfo info, List<Long> groupIdList) {
        List<HouseworkMember> houseworkMemberList = queryFactory.selectFrom(houseworkMember)
                .where(houseworkMember.houseworkInfo.eq(info)).fetch();
        List<Long> houseworkGroupMemberIdList = houseworkMemberList.stream()
                .map(hm -> hm.getGroupMember().getId())
                .collect(Collectors.toList());

        // houseworkGroupMemberIdList의 요소들이 groupIdList의 요소들과 정확히 일치하면 true 리턴
        if (groupIdList.containsAll(houseworkGroupMemberIdList)) {
            return true;
        } else {
            // 다르다면 HouseworkMember 전부 삭제
            houseworkMemberRepository.deleteAll(houseworkMemberList);
            return false;
        }
    }
}
