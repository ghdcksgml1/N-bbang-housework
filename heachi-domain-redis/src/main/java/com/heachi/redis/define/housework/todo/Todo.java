package com.heachi.redis.define.housework.todo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@ToString
public class Todo {

    private Long id;
    private List<TodoUser> houseworkMembers;
    private String category;
    private String title;
    private String detail;
    private Integer idx;
    private String status;
    private LocalDate date;
    private LocalTime endTime;
    private String verificationPhotoURL;
    private String verifierId;
    private LocalDateTime verificationTime;

    @Builder
    private Todo(Long id, List<TodoUser> houseworkMembers, String category, String title, String detail, Integer idx,
                String status, LocalDate date, LocalTime endTime, String verificationPhotoURL, String verifierId,
                LocalDateTime verificationTime) {
        this.id = id;
        this.houseworkMembers = houseworkMembers;
        this.category = category;
        this.title = title;
        this.detail = detail;
        this.idx = idx;
        this.status = status;
        this.date = date;
        this.endTime = endTime;
        this.verificationPhotoURL = verificationPhotoURL;
        this.verifierId = verifierId;
        this.verificationTime = verificationTime;
    }
}
