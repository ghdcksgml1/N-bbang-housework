package com.heachi.notify.api.service.auth;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.auth.AuthException;
import com.heachi.admin.common.exception.jwt.JwtException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.clients.auth.AuthClients;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import java.net.ConnectException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthClients authClients;

    // 토큰으로 유저 아이디 가져오기
    public Mono<String> getUserId(String token) {

        return authClients.getUserInfo(token)
                .onErrorMap(t -> new AuthException(ExceptionMessage.AUTH_SERVER_NOT_RESPOND))
                .map(userInfoResponseJsonResult -> {
                    if (userInfoResponseJsonResult.getResObj() == null) {
                        throw new JwtException(ExceptionMessage.JWT_USER_NOT_FOUND);
                    }

                    return userInfoResponseJsonResult.getResObj().getPlatformId();
                });
    }
}
