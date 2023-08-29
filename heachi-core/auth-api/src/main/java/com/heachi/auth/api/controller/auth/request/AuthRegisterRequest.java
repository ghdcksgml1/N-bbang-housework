package com.heachi.auth.api.controller.auth.request;

import com.heachi.mysql.define.user.constant.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegisterRequest {
    @NotEmpty
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    // 숫자 값이므로 null이 아니어야 하니까 @NotEmpty 대신 @NotNull 사용 -> 빈 문자열("") 허용
    @NotNull
    @Pattern(regexp = "^\\d{11}$",
            message = "전화번호는 11자리의 숫자로 입력해야 합니다.")
    private String phoneNumber;
}