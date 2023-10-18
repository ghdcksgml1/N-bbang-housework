package com.heachi.housework.api.service.housework.todo.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TodoSelectRequest {

    Long groupId;
    LocalDate date;

    @Builder
    private TodoSelectRequest(Long groupId, LocalDate date) {
        this.groupId = groupId;
        this.date = date;
    }
}
