package com.heachi.housework.api.controller.housework.info;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.auth.AuthException;
import com.heachi.admin.common.exception.group.member.GroupMemberException;
import com.heachi.admin.common.exception.housework.HouseworkException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.clients.auth.response.UserInfoResponse;
import com.heachi.housework.api.controller.housework.info.request.HouseworkInfoAddRequest;
import com.heachi.housework.api.controller.housework.info.response.HouseworkInfoAddResponse;
import com.heachi.housework.api.service.auth.AuthExternalService;
import com.heachi.housework.api.service.housework.info.HouseworkInfoService;
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoAddServiceRequest;
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

    @PostMapping("/add/{groupId}")
    public JsonResult<?> houseworkAdd(
            @RequestHeader(name = "Authorization") String authorization,
            @PathVariable(name = "groupId") Long groupId,
            @RequestBody HouseworkInfoAddRequest request
    ) {
        // Auth 서버로 요청자 인증 요청 - 해당 그룹원인지 판별하고 상태가 ACCEPT인지 확인
        try {
            UserInfoResponse requestUser = authExternalService.userAuthenticateAndGroupMatch(authorization, groupId);

            HouseworkInfoAddResponse addResponse = houseworkInfoService.houseworkAdd(requestUser, HouseworkInfoAddServiceRequest.of(request));

            return JsonResult.successOf(addResponse);
        } catch (AuthException | HouseworkException e) {
            return JsonResult.failOf(e.getMessage());
        }
    }
}
