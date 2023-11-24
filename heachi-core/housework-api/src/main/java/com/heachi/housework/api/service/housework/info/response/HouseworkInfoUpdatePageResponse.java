package com.heachi.housework.api.service.housework.info.response;

import com.heachi.housework.api.controller.housework.info.request.HouseworkInfoCreateRequest;
import com.heachi.housework.api.service.housework.category.response.HouseworkCategoryResponse;
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoCreateServiceRequest;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType;
import com.heachi.mysql.define.housework.todo.HouseworkTodo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@ToString
public class HouseworkInfoUpdatePageResponse {
    String title;
    Long houseworkCategoryId;
    List<Long> groupMemberIdList;
    HouseworkPeriodType type;
    LocalDate localDate;
    String weekDate;
    String monthDate;
    LocalTime endTime;

    @Builder
    public HouseworkInfoUpdatePageResponse(String title, Long houseworkCategoryId, List<Long> groupMemberIdList, HouseworkPeriodType type, LocalDate localDate, String weekDate, String monthDate, LocalTime endTime) {
        this.title = title;
        this.houseworkCategoryId = houseworkCategoryId;
        this.groupMemberIdList = groupMemberIdList;
        this.type = type;
        this.localDate = localDate;
        this.weekDate = weekDate;
        this.monthDate = monthDate;
        this.endTime = endTime;
    }

    public static HouseworkInfoUpdatePageResponse of(HouseworkTodo todo, HouseworkCategory category, List<Long> groupMemberIdList) {

        return HouseworkInfoUpdatePageResponse.builder()
                .title(todo.getTitle())
                .houseworkCategoryId(category.getId())
                .groupMemberIdList(groupMemberIdList)
                .type(HouseworkPeriodType.HOUSEWORK_PERIOD_DAY)
                .localDate(todo.getDate())
                .endTime(todo.getEndTime())
                .build();
    }

    public static HouseworkInfoUpdatePageResponse of(HouseworkInfo info, List<Long> groupMemberIdList) {

        return HouseworkInfoUpdatePageResponse.builder()
                .title(info.getTitle())
                .houseworkCategoryId(info.getHouseworkCategory().getId())
                .groupMemberIdList(groupMemberIdList)
                .type(info.getType())
                .localDate(info.getDayDate())
                .weekDate(info.getWeekDate())
                .monthDate(info.getMonthDate())
                .endTime(info.getEndTime())
                .build();
    }
}