package com.heachi.mysql.define.housework.save;

import com.heachi.mysql.define.BaseEntity;
import com.heachi.mysql.define.group.info.GroupInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "HOUSEWORK_SAVE")
@Table(indexes = { @Index(name = "HOUSEWORK_SAVE_GROUP_INFO_INDEX", columnList = "GROUP_INFO_ID") })
public class HouseworkSave extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOUSEWORK_SAVE_ID")
    private Long id;                // 집안일 저장 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_INFO_ID")
    private GroupInfo groupInfo;    // 저장한 그룹

    @Column(name = "NAME", nullable = false)
    private String name;            // 저장한 집안일 이름

    @Builder
    private HouseworkSave(GroupInfo groupInfo, String name) {
        this.groupInfo = groupInfo;
        this.name = name;
    }
}
