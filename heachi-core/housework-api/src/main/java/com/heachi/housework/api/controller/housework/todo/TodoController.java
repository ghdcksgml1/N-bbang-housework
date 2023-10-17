package com.heachi.housework.api.controller.housework.todo;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.clients.auth.response.UserInfoResponse;
import com.heachi.housework.api.service.auth.AuthExternalService;
import com.heachi.housework.api.service.housework.todo.TodoService;
import com.heachi.housework.api.service.housework.todo.request.TodoSelectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/housework/todo")
public class TodoController {

    private final AuthExternalService authExternalService;
    private final TodoService todoService;

    // Todo List 가져오기
    @GetMapping("/{groupId}")
    public JsonResult<?> selectTodo(@RequestHeader(name = "Authorization") String authorization,
                                    @PathVariable(name = "groupId") Long groupId,
                                    @RequestParam(value = "date") LocalDate date) {
        UserInfoResponse userInfo = authExternalService.userAuthenticateAndGroupMatch(authorization, groupId);

        return JsonResult.successOf(todoService.cachedSelectTodo(
                TodoSelectRequest.builder().groupId(groupId).date(date).build()));
    }
}
