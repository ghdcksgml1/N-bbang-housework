package com.heachi.mysql.define.housework.member;

import com.heachi.mysql.define.BaseEntity;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.housework.info.HouseworkInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "HOUSEWORK_MEMBER")
public class HouseworkMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOUSEWORK_MEMBER_ID")
    private Long id;                        // 집안일 담당자 아아디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOUSEWORK_INFO_ID")
    private HouseworkInfo houseworkInfo;    // 집안일 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_MEMBER_ID")
    private GroupMember groupMember;        // 그룹 구성원

    @Builder
    private HouseworkMember(HouseworkInfo houseworkInfo, GroupMember groupMember) {
        this.houseworkInfo = houseworkInfo;
        this.groupMember = groupMember;
    }

}
