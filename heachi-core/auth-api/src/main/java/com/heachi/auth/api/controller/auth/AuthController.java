package com.heachi.auth.api.controller.auth;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.oauth.OAuthException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.auth.api.controller.auth.request.AuthRegisterRequest;
import com.heachi.auth.api.controller.auth.response.AuthRegisterResponse;
import com.heachi.auth.api.service.auth.AuthService;
import com.heachi.auth.api.service.auth.request.AuthServiceRegisterRequest;
import com.heachi.auth.api.service.auth.response.AuthServiceLoginResponse;
import com.heachi.auth.api.service.oauth.OAuthService;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import com.heachi.mysql.define.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthService authService;
    private final OAuthService oAuthService;

    @GetMapping("/{platformType}/loginPage")
    public JsonResult<String> loginPage(
            @PathVariable("platformType") UserPlatformType platformType,
            HttpServletRequest request) {
        String loginPage = oAuthService.loginPage(platformType, request.getSession().getId());

        return JsonResult.successOf(loginPage);
    }

    @GetMapping("/{platformType}/login")
    public JsonResult<String> login(
            @PathVariable("platformType") UserPlatformType platformType,
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletRequest request) {
        String requestState = request.getSession().getId();

        // state 값 유효성 검증
        if (!state.equals(requestState)) {
            throw new OAuthException(ExceptionMessage.OAUTH_INVALID_STATE);
        }

        AuthServiceLoginResponse loginResponse = authService.login(platformType, code, request.getSession().getId());

        return JsonResult.successOf(loginResponse);
    }

    @PostMapping("/{platformType}/register")
    public JsonResult<?> register(
            @PathVariable("platformType") UserPlatformType platformType,
            @RequestBody @Valid AuthRegisterRequest request,
            BindingResult bindingResult) {

        // AuthRegisterRequest 유효성 검사 실패시 실패한 필드의 에러 메세지를 담아 실패 리턴
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));

            return JsonResult.failOf(errorMessages);
        }

        // Service용 DTO로 변환
        AuthServiceRegisterRequest registerRequest = AuthServiceRegisterRequest.builder()
                .platformId(request.getPlatformId())
                .role(request.getRole())
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .profileImageUrl(request.getProfileImageUrl())
                .build();

        AuthRegisterResponse registerResponse = authService.register(platformType, registerRequest);

        return JsonResult.successOf(registerResponse);
    }
}
