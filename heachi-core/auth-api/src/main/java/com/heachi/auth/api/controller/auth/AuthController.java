package com.heachi.auth.api.controller.auth;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.auth.api.controller.auth.request.AuthRegisterRequest;
import com.heachi.auth.api.service.auth.response.AuthServiceLoginResponse;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    @PostMapping("/{platformType}/login")
    public JsonResult<AuthServiceLoginResponse> login(
            @PathVariable("platformType") UserPlatformType platformType,
            @RequestParam("code") String code) {

        return JsonResult.successOf("AuthServiceLoginResponse");
    }

    @PostMapping("/{platformType}/register")
    public JsonResult<?> register(
            @PathVariable("platformType") UserPlatformType platformType,
            @RequestBody AuthRegisterRequest request) {
        return JsonResult.successOf();
    }
}
