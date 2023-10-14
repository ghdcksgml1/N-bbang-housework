package com.heachi.housework.api.controller.response;

import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class HouseworkAddResponseDTO {
    private List<HouseworkMember> houseworkMembers;
    private HouseworkCategory houseworkCategory;
    private String title;
    private String detail;
    private HouseworkPeriodType type;
    private Date dayDate;
    private String weekDate;
    private String monthDate;
    private Date endTime;

    @Builder
    public HouseworkAddResponseDTO(List<HouseworkMember> houseworkMembers, HouseworkCategory houseworkCategory, String title, String detail, HouseworkPeriodType type, Date dayDate, String weekDate, String monthDate, Date endTime) {
        this.houseworkMembers = houseworkMembers;
        this.houseworkCategory = houseworkCategory;
        this.title = title;
        this.detail = detail;
        this.type = type;
        this.dayDate = dayDate;
        this.weekDate = weekDate;
        this.monthDate = monthDate;
        this.endTime = endTime;
    }
}
