package com.heachi.auth.api.controller.auth;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.auth.api.controller.auth.request.AuthRegisterRequest;
import com.heachi.auth.api.service.auth.AuthService;
import com.heachi.auth.api.service.auth.response.AuthServiceLoginResponse;
import com.heachi.auth.api.service.oauth.OAuthService;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final OAuthService oAuthService;

    @GetMapping("/{platformType}/loginPage")
    public JsonResult<String> loginPage(
            @PathVariable("platformType") UserPlatformType platformType) {

        return JsonResult.successOf("good");
    }

    @GetMapping("/{platformType}/login")
    public JsonResult<AuthServiceLoginResponse> login(
            @PathVariable("platformType") UserPlatformType platformType,
            @RequestParam("code") String code,
            @RequestParam("state") String state) {

        return JsonResult.successOf("AuthServiceLoginResponse");
    }

    @PostMapping("/{platformType}/register")
    public JsonResult<?> register(
            @PathVariable("platformType") UserPlatformType platformType,
            @RequestBody AuthRegisterRequest request) {
        return JsonResult.successOf();
    }
}