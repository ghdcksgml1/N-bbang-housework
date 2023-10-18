package com.heachi.external.clients.auth;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.annotation.ExternalClients;
import com.heachi.external.clients.auth.response.UserInfoResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

@ExternalClients(baseUrl = "heachi-core.auth-api")
public interface AuthClients {

    @GetExchange(value = "/auth/info")
    public Mono<JsonResult<UserInfoResponse>> getUserInfo(@RequestHeader(value = "Authorization") String headers);
}
