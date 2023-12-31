package com.heachi.mysql.define.housework.info;

import com.heachi.mysql.define.BaseEntity;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "HOUSEWORK_INFO")
public class HouseworkInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOUSEWORK_INFO_ID")
    private Long id;                                                    // 집안일 정보 아이디

    @OneToMany(mappedBy = "houseworkInfo")
    private List<HouseworkMember> houseworkMembers = new ArrayList<>(); // 집안일 담당자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_INFO_ID")
    private GroupInfo groupInfo;                                        // 어떤 그룹의 집안일 정보인지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOUSEWORK_CATEGORY_ID")
    private HouseworkCategory houseworkCategory;                        // 집안일 카테고리

    @Column(name = "TITLE", nullable = false)
    private String title;                                               // 집안일 제목

    @Column(name = "DETAIL")
    private String detail;                                              // 집안일 내용

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private HouseworkPeriodType type;                                   // 집안일 주기 타입 (한번, 매일, 매주, 매달)

    @Temporal(TemporalType.DATE)
    @Column(name = "DAY_DATE")
    private LocalDate dayDate;                                          // 단건: 날짜 정보

    @Column(name = "WEEK_DATE")
    private String weekDate;                                            // 매주: 요일 정보 (월~일: 1~7)

    @Column(name = "MONTH_DATE")
    private String monthDate;                                           // 매달: 일 정보 (1,23,25)

    @Column(name = "END_TIME")
    private LocalTime endTime;                                       // 집안일 마감 시간 (시간정보만)

    @Builder
    private HouseworkInfo(GroupInfo groupInfo, HouseworkCategory houseworkCategory,
                          String title, String detail, HouseworkPeriodType type, LocalDate dayDate, String weekDate,
                          String monthDate, LocalTime endTime) {
        this.groupInfo = groupInfo;
        this.houseworkCategory = houseworkCategory;
        this.title = title;
        this.detail = detail;
        this.type = type;
        this.dayDate = dayDate;
        this.weekDate = weekDate;
        this.monthDate = monthDate;
        this.endTime = endTime;
    }

    public void updateHouseworkInfo(String title, String detail, HouseworkCategory category,
                                    HouseworkPeriodType type, LocalDate date, LocalTime endTime,
                                    String weekDate, String monthDate) {
        this.title = title;
        this.detail = detail;
        this.houseworkCategory = category;
        this.type = type;
        this.dayDate = date;
        this.weekDate = weekDate;
        this.monthDate = monthDate;
        this.endTime = endTime;

    }
}
