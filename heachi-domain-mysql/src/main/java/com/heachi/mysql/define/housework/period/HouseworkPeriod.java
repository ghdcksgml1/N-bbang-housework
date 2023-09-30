package com.heachi.mysql.define.housework.period;

import com.heachi.mysql.define.BaseEntity;
import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.period.constant.HouseworkPeriodType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.Date;

@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "HOUSEWORK_PERIOD")
public class HouseworkPeriod extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOUSEWORK_PERIOD_ID")
    private Long id;                        // 집안일 주기 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOUSEWORK_INFO_ID")
    private HouseworkInfo houseworkInfo;    // 집안일 정보

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    @ColumnDefault(value = "'HOUSEWORK_PERIOD_DAY'")
    private HouseworkPeriodType type;       // 주기 종류

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE", nullable = false)
    private Date date;                      // 날짜

    @Temporal(TemporalType.TIME)
    @Column(name = "END_TIME", nullable = false)
    private Date endTime;                   // 집안일 마감시간

    @Builder
    public HouseworkPeriod(HouseworkInfo houseworkInfo, HouseworkPeriodType type, Date date, Date endTime) {
        this.houseworkInfo = houseworkInfo;
        this.type = type;
        this.date = date;
        this.endTime = endTime;
    }
}
