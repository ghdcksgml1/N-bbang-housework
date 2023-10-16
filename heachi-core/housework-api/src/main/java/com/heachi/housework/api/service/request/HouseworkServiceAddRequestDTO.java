package com.heachi.housework.api.service.request;

import com.heachi.housework.api.controller.request.HouseworkAddRequestDTO;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.util.Date;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseworkServiceAddRequestDTO {
    private List<GroupMember> groupMembers;
    private HouseworkCategory houseworkCategory;
    private String title;
    private String detail;
    private HouseworkPeriodType type;
    private Date dayDate;
    private String weekDate;
    private String monthDate;
    private Date endTime;

    public static HouseworkServiceAddRequestDTO of(HouseworkAddRequestDTO request) {
        return HouseworkServiceAddRequestDTO.builder()
                .groupMembers(request.getGroupMembers())
                .houseworkCategory(request.getHouseworkCategory())
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
