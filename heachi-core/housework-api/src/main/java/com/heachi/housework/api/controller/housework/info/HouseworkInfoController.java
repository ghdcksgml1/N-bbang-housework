package com.heachi.housework.api.controller.housework.info;

import com.heachi.admin.common.exception.auth.AuthException;
import com.heachi.admin.common.exception.housework.HouseworkException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.clients.auth.response.UserInfoResponse;
import com.heachi.housework.api.controller.housework.info.request.HouseworkInfoCreateRequest;
import com.heachi.housework.api.service.auth.AuthExternalService;
import com.heachi.housework.api.service.housework.info.HouseworkInfoService;
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoCreateServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/housework")
@RestController
public class HouseworkInfoController {
    private final HouseworkInfoService houseworkInfoService;
    private final AuthExternalService authExternalService;

    @PostMapping("/{groupId}")
    public JsonResult<?> createHouseworkInfo(
            @RequestHeader(name = "Authorization") String authorization,
            @PathVariable(name = "groupId") Long groupId,
            @RequestBody HouseworkInfoCreateRequest request
    ) {
        // Auth 서버로 요청자 인증 요청 - 해당 그룹원인지 판별하고 상태가 ACCEPT인지 확인
        try {
            UserInfoResponse requestUser = authExternalService.userAuthenticateAndGroupMatch(authorization, groupId);

            houseworkInfoService.createHouseworkInfo(requestUser, HouseworkInfoCreateServiceRequest.of(request));

            return JsonResult.successOf("Housework Create Success.");
        } catch (AuthException | HouseworkException e) {
            return JsonResult.failOf(e.getMessage());
        }
    }
}
