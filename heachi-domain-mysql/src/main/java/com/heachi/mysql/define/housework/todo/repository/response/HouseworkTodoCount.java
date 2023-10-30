package com.heachi.mysql.define.housework.todo.repository.response;

import com.heachi.mysql.define.housework.todo.constant.HouseworkTodoStatus;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class HouseworkTodoCount {
    private Long id;                            // groupInfo Id
    private List<HouseworkTodoStatus> status;   // HouseworkTodo의 상태
}
