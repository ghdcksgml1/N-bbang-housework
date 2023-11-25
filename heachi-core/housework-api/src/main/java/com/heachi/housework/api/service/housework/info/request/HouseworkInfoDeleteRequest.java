package com.heachi.housework.api.service.housework.info.request;

import com.heachi.housework.api.controller.housework.info.request.HouseworkInfoDeleteType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class HouseworkInfoDeleteRequest {
    private Long groupId;
    private LocalDate date;
    private Long todoId;
    private HouseworkInfoDeleteType deleteType;

    @Builder
    public HouseworkInfoDeleteRequest(Long groupId, LocalDate date, Long todoId, HouseworkInfoDeleteType deleteType) {
        this.groupId = groupId;
        this.date = date;
        this.todoId = todoId;
        this.deleteType = deleteType;
    }
}
