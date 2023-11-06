package com.heachi.housework.api.controller.housework.todo;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.clients.auth.response.UserInfoResponse;
import com.heachi.housework.api.controller.housework.todo.request.VerifyTodoRequest;
import com.heachi.housework.api.service.auth.AuthExternalService;
import com.heachi.housework.api.service.housework.todo.TodoService;
import com.heachi.housework.api.service.housework.todo.request.TodoSelectRequest;
import com.heachi.housework.api.service.housework.todo.request.VerifyTodoServiceRequest;
import com.heachi.s3.api.service.AwsS3Service;
import jakarta.validation.Valid;
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
    private final AwsS3Service awsS3Service;

    // Todo List 가져오기
    @GetMapping("/{groupId}")
    public JsonResult<?> selectTodo(@RequestHeader(name = "Authorization") String authorization,
                                    @PathVariable(name = "groupId") Long groupId,
                                    @RequestParam(value = "date") LocalDate date) {
        UserInfoResponse userInfo = authExternalService.userAuthenticateAndGroupMatch(authorization, groupId);

        return JsonResult.successOf(todoService.cachedSelectTodo(
                TodoSelectRequest.builder().groupId(groupId).date(date).build()));
    }

    // 집안일 인증하기
    @PostMapping("/verify")
    public JsonResult<?> verifyTodo(@RequestHeader(name = "Authorization") String authorization,
                                    @Valid @ModelAttribute(name = "verifyTodoRequest") VerifyTodoRequest verifyTodoRequest) {
        UserInfoResponse userInfo = authExternalService.userAuthenticateAndGroupMatch(authorization, verifyTodoRequest.getGroupId());
        String uploadedImageURL = awsS3Service.uploadImage(verifyTodoRequest.getFile());
        todoService.verifyTodo(VerifyTodoServiceRequest.builder()
                .verifyImageURL(uploadedImageURL)
                .todoId(verifyTodoRequest.getTodoId())
                .email(userInfo.getEmail())
                .build());

        return JsonResult.successOf("사진이 정상적으로 저장되었습니다.");
    }
}
