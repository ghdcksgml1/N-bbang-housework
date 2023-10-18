package com.heachi.housework.api.controller.housework.info.response;

import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class HouseworkInfoAddResponse {
    private List<Long> houseworkMemberIdList;
    private Long houseworkCategoryId;
    private String title;
    private String detail;
    private HouseworkPeriodType type;
    private LocalDate dayDate;
    private String weekDate;
    private String monthDate;
    private LocalTime endTime;

    @Builder
    public HouseworkInfoAddResponse(List<Long> houseworkMemberIdList, HouseworkCategory houseworkCategory, String title, String detail, HouseworkPeriodType type, LocalDate dayDate, String weekDate, String monthDate, LocalTime endTime) {
        this.houseworkMemberIdList = houseworkMemberIdList;
        this.houseworkCategoryId = houseworkCategory.getId();
        this.title = title;
        this.detail = detail;
        this.type = type;
        this.dayDate = dayDate;
        this.weekDate = weekDate;
        this.monthDate = monthDate;
        this.endTime = endTime;
    }
}
