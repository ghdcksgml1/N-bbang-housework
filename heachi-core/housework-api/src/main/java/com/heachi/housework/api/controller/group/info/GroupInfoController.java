package com.heachi.housework.api.controller.group.info;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.clients.auth.response.UserInfoResponse;
import com.heachi.housework.api.controller.group.info.request.GroupInfoCreateRequest;
import com.heachi.housework.api.service.auth.AuthExternalService;
import com.heachi.housework.api.service.group.info.GroupInfoService;
import com.heachi.housework.api.service.group.info.request.GroupInfoCreateServiceRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/group/info")
public class GroupInfoController {

    private final AuthExternalService authExternalService;
    private final GroupInfoService groupInfoService;

    @PostMapping("/")
    public JsonResult<?> createGroupInfo(@RequestHeader(name = "Authorization") String authorization,
                                         @Valid @RequestBody GroupInfoCreateRequest request) {
        // 유저 인증
        UserInfoResponse userInfoResponse = authExternalService.userAuthenticate(authorization);
        // GroupInfo 생성
        groupInfoService.createGroupInfo(GroupInfoCreateServiceRequest.builder()
                .bgColor(request.getBgColor())
                .colorCode(request.getColorCode())
                .gradient(request.getGradient())
                .name(request.getName())
                .info(request.getInfo())
                .email(userInfoResponse.getEmail())
                .build());

        return JsonResult.successOf();
    }
}
