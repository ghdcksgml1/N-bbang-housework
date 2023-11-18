package com.heachi.housework.api.service.housework.info.request;

import com.heachi.housework.api.controller.housework.info.request.HouseworkInfoCreateRequest;
import com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class HouseworkInfoUpdateServiceRequest {
    private List<Long> groupMemberIdList;
    private Long houseworkCategoryId;
    private String title;
    private String detail;
    private HouseworkPeriodType type;
    private LocalDate dayDate;
    private String weekDate;
    private String monthDate;
    private LocalTime endTime;

    private Long groupId;
    private Long todoId;
    private LocalDate date;

    @Builder
    public HouseworkInfoUpdateServiceRequest(List<Long> groupMemberIdList, Long houseworkCategoryId, String title, String detail, HouseworkPeriodType type, LocalDate dayDate, String weekDate, String monthDate, LocalTime endTime, Long groupId, Long todoId, LocalDate date) {
        this.groupMemberIdList = groupMemberIdList;
        this.houseworkCategoryId = houseworkCategoryId;
        this.title = title;
        this.detail = detail;
        this.type = type;
        this.dayDate = dayDate;
        this.weekDate = weekDate;
        this.monthDate = monthDate;
        this.endTime = endTime;
        this.groupId = groupId;
        this.todoId = todoId;
        this.date = date;
    }

    public static HouseworkInfoUpdateServiceRequest of(HouseworkInfoCreateRequest request, Long groupId, Long todoId, LocalDate date) {

        return HouseworkInfoUpdateServiceRequest.builder()
                .groupMemberIdList(request.getGroupMemberIdList())
                .houseworkCategoryId(request.getHouseworkCategoryId())
                .title(request.getTitle())
                .detail(request.getDetail())
                .type(request.getType())
                .dayDate(request.getDayDate())
                .weekDate(request.getWeekDate())
                .monthDate(request.getMonthDate())
                .endTime(request.getEndTime())
                .groupId(groupId)
                .todoId(todoId)
                .date(date)
                .build();
    }
}
