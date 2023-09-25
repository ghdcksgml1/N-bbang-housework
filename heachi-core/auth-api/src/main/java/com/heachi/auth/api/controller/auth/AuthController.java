package com.heachi.auth.api.controller.auth;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.jwt.JwtException;
import com.heachi.admin.common.exception.oauth.OAuthException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.auth.api.controller.auth.request.AuthRegisterRequest;
import com.heachi.auth.api.controller.auth.response.UserSimpleInfoResponse;
import com.heachi.auth.api.controller.token.response.ReissueAccessTokenResponse;
import com.heachi.auth.api.service.auth.AuthService;
import com.heachi.auth.api.service.auth.request.AuthServiceRegisterRequest;
import com.heachi.auth.api.service.auth.response.AuthServiceLoginResponse;
import com.heachi.auth.api.service.oauth.OAuthService;
import com.heachi.auth.api.service.state.LoginStateService;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import io.swagger.v3.core.util.Json;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthService authService;
    private final OAuthService oAuthService;
    private final LoginStateService loginStateService;

    @GetMapping("/{platformType}/loginPage")
    public JsonResult<String> loginPage(
            @PathVariable("platformType") UserPlatformType platformType) {
        String loginState = loginStateService.generateLoginState();
        String loginPage = oAuthService.loginPage(platformType, loginState);

        return JsonResult.successOf(loginPage);
    }

    @GetMapping("/{platformType}/login")
    public JsonResult<String> login(
            @PathVariable("platformType") UserPlatformType platformType,
            @RequestParam("code") String code,
            @RequestParam("state") String loginState) {
        if (!loginStateService.isValidLoginState(loginState)) {
            throw new OAuthException(ExceptionMessage.OAUTH_INVALID_STATE);
        }
        AuthServiceLoginResponse loginResponse = authService.login(platformType, code, loginState);

        return JsonResult.successOf(loginResponse);
    }

    @PostMapping("/register")
    public JsonResult<?> register(
            @Valid @RequestBody AuthRegisterRequest request) {

        AuthServiceLoginResponse registerResponse = authService.register(AuthServiceRegisterRequest.of(request));

        return JsonResult.successOf(registerResponse);
    }

    @GetMapping("/info")
    public JsonResult<UserSimpleInfoResponse> userInfo(@AuthenticationPrincipal User user) {

        return JsonResult.successOf(UserSimpleInfoResponse.of(user));
    }

    @GetMapping("/logout")
    public JsonResult<?> logout(@RequestHeader(name = "Authorization") String token) {
        List<String> tokens = Arrays.asList(token.split(" "));

        if (tokens.size() == 3) {
            authService.logout(tokens.get(2));

            return JsonResult.successOf("Logout successfully.");
        } else {
            return JsonResult.failOf(ExceptionMessage.JWT_INVALID_HEADER.getText());
        }

    }

    @PostMapping("/delete")
    public JsonResult<?> userDelete(@AuthenticationPrincipal User user) {
        authService.userDelete(user.getEmail());

        return JsonResult.successOf();
    }

    @PostMapping("/reissue")
    public JsonResult<?> reissueAccessToken(@RequestHeader(name = "Authorization") String token) {
        List<String> tokens = Arrays.asList(token.split(" "));

        if (tokens.size() == 3) {
            ReissueAccessTokenResponse reissueResponse = authService.reissueAccessToken(tokens.get(2));

            return JsonResult.successOf(reissueResponse);
        } else {
            return JsonResult.failOf(ExceptionMessage.JWT_INVALID_HEADER.getText());
        }
    }
}