package com.heachi.housework.api.controller.housework.todo.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@ToString
@AllArgsConstructor
public class VerifyTodoRequest {
    @NotNull
    private MultipartFile file; // (필수) 사진
    @NotNull
    private Long todoId;        // (필수) 집안일 아이디
    @NotNull
    private Long groupId;       // (필수) 그룹 아이디
    private String comment;     // (선택) 사진에 대한 설명
}
