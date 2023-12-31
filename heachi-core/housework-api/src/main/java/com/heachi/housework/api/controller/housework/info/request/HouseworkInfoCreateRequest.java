package com.heachi.housework.api.controller.housework.info.request;

import com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class HouseworkInfoCreateRequest {
    @NotEmpty
    private List<Long> groupMemberIdList;

    private Long houseworkCategoryId;

    @NotEmpty
    private String title; // 집안일 제목
    private String detail; // 집안일 내용

    @NotNull
    @Enumerated(EnumType.STRING)
    private HouseworkPeriodType type; // 집안일 주기 타입 (한번, 매일, 매주, 매달)

    private LocalDate dayDate; // 단건: 날짜 정보

    @Pattern(regexp = "^[1-7]$",
            message = "요일 정보는 1~7 사이의 문자열이어야 합니다.")  // (1~7)
    private String weekDate;

    @Pattern(regexp = "^(0*[1-9]|[12][0-9]|3[0-1])$",
            message = "일 정보는 1~31 사이의 문자열이어야 합니다.") // (1~31)
    private String monthDate;

    @NotNull
    private LocalTime endTime;
}
