package com.heachi.housework.api.controller.group.info;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.clients.auth.response.UserInfoResponse;
import com.heachi.housework.api.controller.group.info.request.GroupInfoRegisterRequest;
import com.heachi.housework.api.service.auth.AuthExternalService;
import com.heachi.housework.api.service.group.info.GroupInfoService;
import com.heachi.housework.api.service.group.info.response.GroupInfoUserGroupServiceResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.heachi.housework.api.controller.group.info.request.GroupInfoCreateRequest;
import com.heachi.housework.api.service.auth.AuthExternalService;
import com.heachi.housework.api.service.group.info.request.GroupInfoCreateServiceRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        return JsonResult.successOf("그룹이 성공적으로 생성되었습니다.");
    }

    // User가 가입한 Group 정보를 리턴한다.
    @ApiResponse(responseCode = "200", description = "그룹 리스트 조회 성공", content = @Content(schema = @Schema(implementation = GroupInfoUserGroupServiceResponse.class, type = "array")))
    @GetMapping("/list")
    public JsonResult<?> userGroupInfoList(@RequestHeader(name = "Authorization") String authorization) {
        UserInfoResponse userInfo = authExternalService.userAuthenticate(authorization);

        return JsonResult.successOf(groupInfoService.userGroupInfoList(userInfo.getEmail()));
    }

    // joinCode를 통해 그룹 가입하기
    @PostMapping("/join")
    public JsonResult<?> joinGroupInfo(@RequestHeader(name = "Authorization") String authorization,
                                       @RequestParam(name = "joinCode") String joinCode) {
        UserInfoResponse userInfo = authExternalService.userAuthenticate(authorization);
        groupInfoService.joinGroupInfo(userInfo.getEmail(), joinCode);

        return JsonResult.successOf("성공적으로 그룹에 가입했습니다.");
    }

    // 그룹 가입 요청 처리
    @PostMapping("/register")
    public JsonResult<?> joinGroupRequestHandler(@RequestHeader(name = "Authorization") String authorization,
                                                 @Valid @RequestBody GroupInfoRegisterRequest request) {
        // Auth 서버에서 사용자 인증
        UserInfoResponse userInfo = authExternalService.userAuthenticate(authorization);
        groupInfoService.joinRequestHandler(userInfo.getEmail(), request);

        return JsonResult.successOf("그룹 가입 요청을 성공적으로 수행했습니다.");
    }
}
