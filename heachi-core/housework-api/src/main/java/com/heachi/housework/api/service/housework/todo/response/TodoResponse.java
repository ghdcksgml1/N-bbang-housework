package com.heachi.housework.api.service.housework.todo.response;

import com.heachi.mysql.define.housework.todo.HouseworkTodo;
import com.heachi.mysql.define.housework.todo.constant.HouseworkTodoStatus;
import com.heachi.mysql.define.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ToString
public class TodoResponse {

    private Long id;
    private List<TodoUser> houseworkMembers;
    private String category;
    private String title;
    private String detail;
    private Integer idx;
    private HouseworkTodoStatus status;
    private LocalDate date;
    private LocalTime endTime;
    private String verificationPhotoURL;
    private String verifierId;
    private LocalDateTime verificationTime;

    @Builder
    private TodoResponse(Long id, List<TodoUser> houseworkMembers, String category, String title, String detail,
                         Integer idx, HouseworkTodoStatus status, LocalDate date, LocalTime endTime,
                         String verificationPhotoURL, String verifierId, LocalDateTime verificationTime) {
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

    public static TodoResponse of(HouseworkTodo todo, Map<Long, User> userMap) {

        return TodoResponse.builder()
                .id(todo.getId())
                .houseworkMembers(todo.getHouseworkMember() != "" ? Arrays.asList(todo.getHouseworkMember().split(",")).stream()
                        .map(Long::parseLong)
                        .map(i -> TodoUser.of(userMap.get(i)))
                        .collect(Collectors.toList()) : null)
                .category(todo.getCategory())
                .title(todo.getTitle())
                .detail(todo.getDetail())
                .idx(todo.getIdx())
                .status(todo.getStatus())
                .date(todo.getDate())
                .endTime(todo.getEndTime())
                .verificationPhotoURL(todo.getVerificationPhotoURL())
                .verifierId(todo.getVerifierId())
                .verificationTime(todo.getVerificationTime())
                .build();
    }
}
