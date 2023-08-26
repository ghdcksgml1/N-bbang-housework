package com.heachi.auth.api.controller.auth;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.oauth.OAuthException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.auth.api.controller.auth.request.AuthRegisterRequest;
import com.heachi.auth.api.service.auth.AuthService;
import com.heachi.auth.api.service.auth.response.AuthServiceLoginResponse;
import com.heachi.auth.api.service.oauth.OAuthService;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
            @RequestBody AuthRegisterRequest request) {
        return JsonResult.successOf();
    }

    @GetMapping("/info")
    public JsonResult<?> userInfo(@AuthenticationPrincipal UserDetails user) {

        return JsonResult.successOf(user);
    }
}
