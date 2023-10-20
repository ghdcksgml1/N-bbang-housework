package com.heachi.housework.api.service.housework.info.request;

import com.heachi.housework.api.controller.housework.info.request.HouseworkInfoCreateRequest;
import com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class HouseworkInfoCreateServiceRequest {
    private List<Long> groupMemberIdList;
    private Long houseworkCategoryId;
    private String title;
    private String detail;
    private HouseworkPeriodType type;
    private LocalDate dayDate;
    private String weekDate;
    private String monthDate;
    private LocalTime endTime;

    @Builder
    private HouseworkInfoCreateServiceRequest(List<Long> groupMemberIdList, Long houseworkCategoryId, String title,
                                             String detail, HouseworkPeriodType type, LocalDate dayDate, String weekDate,
                                             String monthDate, LocalTime endTime) {
        this.groupMemberIdList = groupMemberIdList == null ? new ArrayList<>() : groupMemberIdList;
        this.houseworkCategoryId = houseworkCategoryId;
        this.title = title;
        this.detail = detail;
        this.type = type;
        this.dayDate = dayDate;
        this.weekDate = weekDate;
        this.monthDate = monthDate;
        this.endTime = endTime;
    }

    public static HouseworkInfoCreateServiceRequest of(HouseworkInfoCreateRequest request) {

        return HouseworkInfoCreateServiceRequest.builder()
                .groupMemberIdList(request.getGroupMemberIdList())
                .houseworkCategoryId(request.getHouseworkCategoryId())
                .title(request.getTitle())
                .detail(request.getDetail())
                .type(request.getType())
                .dayDate(request.getDayDate())
                .weekDate(request.getWeekDate())
                .monthDate(request.getMonthDate())
                .endTime(request.getEndTime())
                .build();
    }
}
