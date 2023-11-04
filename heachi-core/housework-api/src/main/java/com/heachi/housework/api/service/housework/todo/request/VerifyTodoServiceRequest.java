package com.heachi.housework.api.service.housework.todo.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VerifyTodoServiceRequest {
    private Long todoId;            // 집안일 아이디
    private String email;           // 인증 요청 이메일
    private String verifyImageURL;  // 인증 사진

    @Builder
    private VerifyTodoServiceRequest(Long todoId, String email, String verifyImageURL) {
        this.todoId = todoId;
        this.email = email;
        this.verifyImageURL = verifyImageURL;
    }
}
