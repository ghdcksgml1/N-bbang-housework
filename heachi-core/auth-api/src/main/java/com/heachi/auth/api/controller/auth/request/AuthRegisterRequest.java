package com.heachi.auth.api.controller.auth.request;

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

    // 문자열 값이며 비어있지 않아야 하므로 @NotEmpty 사용 -> 빈 문자열("") 불허
    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{7,16}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자여야 합니다")
    private String platformId;

    // 플랫폼 타입 회원 정보에 필요할까??
    @NotEmpty
    private String platformType;

    @NotEmpty
    private String role;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z가-힣\\\\s]{2,15}",
            message = "이름은 영문자, 한글, 공백포함 2글자부터 15글자까지 가능합니다.")
    private String name;

    // user@example.com
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "이메일 형식을 맞춰야합니다")
    private String email;


    // 숫자 값이므로 null이 아니어야 하니까 @NotEmpty 대신 @NotNull 사용 -> 빈 문자열("") 허용
    @NotNull
    @Pattern(regexp = "^\\d{11}$\n",
            message = "전화번호는 11자리의 숫자로 입력해야 합니다.")
    private String phoneNumber;

    @NotEmpty
    private String profileImageUrl;
}
