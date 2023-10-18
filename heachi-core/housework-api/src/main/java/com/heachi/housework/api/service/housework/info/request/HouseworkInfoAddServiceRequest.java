package com.heachi.housework.api.service.housework.info.request;

import com.heachi.housework.api.controller.housework.info.request.HouseworkInfoAddRequest;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseworkInfoAddServiceRequest {
    private List<Long> groupMemberIdList;
    private Long houseworkCategoryId;
    private String title;
    private String detail;
    private HouseworkPeriodType type;
    private LocalDate dayDate;
    private String weekDate;
    private String monthDate;
    private LocalTime endTime;

    public static HouseworkInfoAddServiceRequest of(HouseworkInfoAddRequest request) {
        return HouseworkInfoAddServiceRequest.builder()
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
