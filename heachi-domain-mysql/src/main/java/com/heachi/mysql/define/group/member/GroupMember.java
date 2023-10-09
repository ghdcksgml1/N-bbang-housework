package com.heachi.mysql.define.group.member;

import com.heachi.mysql.define.BaseEntity;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.member.constant.GroupMemberRole;
import com.heachi.mysql.define.group.member.constant.GroupMemberStatus;
import com.heachi.mysql.define.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@DynamicInsert // Entity save 시점에 null 값은 배제하고 Insert Query를 날려줌
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "GROUP_MEMBER")
public class GroupMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GROUP_MEMBER_ID")
    private Long id;                    // 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_INFO_ID", nullable = false)
    private GroupInfo groupInfo;        // 그룹 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;                  // 유저

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    @ColumnDefault(value = "'GROUP_MEMBER'")
    private GroupMemberRole role;       // 그룹 내 역할

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    @ColumnDefault(value = "'WAITING'")
    private GroupMemberStatus status;   // 구성원 상태 (대기 중, 수락, 탈퇴)

    @Builder
    private GroupMember(GroupInfo groupInfo, User user, GroupMemberRole role, GroupMemberStatus status) {
        this.groupInfo = groupInfo;
        this.user = user;
        this.role = role;
        this.status = status;
    }

}
