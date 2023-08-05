package com.heachi.auth.api.controller.auth;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.auth.api.controller.auth.request.AuthLoginRequest;
import com.heachi.auth.api.controller.auth.request.AuthRegisterRequest;
import com.heachi.auth.api.service.auth.response.AuthServiceLoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    @PostMapping("/login")
    public JsonResult<AuthServiceLoginResponse> login(@RequestBody AuthLoginRequest request) {

        return JsonResult.successOf("AuthServiceLoginResponse");
    }

    @PostMapping("/register")
    public JsonResult<?> register(
            @RequestParam("code") String code,
            @RequestBody AuthRegisterRequest request
    ) {
        return JsonResult.successOf();
    }
}
