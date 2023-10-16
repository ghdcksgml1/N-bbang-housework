package com.heachi.housework.api.controller.request;

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

import java.util.Date;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseworkAddRequestDTO {
    @NotEmpty
    private List<GroupMember> groupMembers;

    @Enumerated(EnumType.STRING)
    @NotNull
    private HouseworkCategory houseworkCategory;

    @NotEmpty
    private String title; // 집안일 제목
    private String detail; // 집안일 내용

    @Enumerated(EnumType.STRING)
    @NotNull
    private HouseworkPeriodType type; // 집안일 주기 타입 (한번, 매일, 매주, 매달)

    private Date dayDate; // 단건: 날짜 정보

    @Pattern(regexp = "^[0-6]$",
            message = "요일 정보는 0~6 사이의 문자열이어야 합니다.")  // (0~6)
    private String weekDate;

    @Pattern(regexp = "^(0*[1-9]|[12][0-9]|3[0-1])$",   // (1~31)
            message = "일 정보는 1~31 사이의 문자열이어야 합니다.")  // (0~6)
    private String monthDate;

    @NotNull
    private Date endTime;
}
