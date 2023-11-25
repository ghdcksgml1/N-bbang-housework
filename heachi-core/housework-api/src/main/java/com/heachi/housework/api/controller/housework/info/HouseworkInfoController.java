package com.heachi.housework.api.controller.housework.info;

import com.heachi.admin.common.exception.HeachiException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.housework.api.controller.housework.info.request.HouseworkInfoCreateRequest;
import com.heachi.housework.api.controller.housework.info.request.HouseworkInfoDeleteType;
import com.heachi.housework.api.service.auth.AuthExternalService;
import com.heachi.housework.api.service.housework.info.HouseworkInfoService;
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoCreateServiceRequest;
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoDeleteRequest;
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoUpdateServiceRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/housework")
@RestController
public class HouseworkInfoController {
    private final HouseworkInfoService houseworkInfoService;
    private final AuthExternalService authExternalService;

    @PostMapping("/{groupId}")
    public JsonResult<?> createHouseworkInfo(@RequestHeader(name = "Authorization") String authorization,
                                             @PathVariable(name = "groupId") Long groupId,
                                             @Valid @RequestBody HouseworkInfoCreateRequest request
    ) {
        // Auth 서버로 요청자 인증 요청 - 해당 그룹원인지 판별하고 상태가 ACCEPT인지 확인
        try {
            authExternalService.userAuthenticateAndGroupMatch(authorization, groupId);
            houseworkInfoService.createHouseworkInfo(HouseworkInfoCreateServiceRequest.of(request, groupId));

            return JsonResult.successOf("Housework Create Success.");
        } catch (HeachiException e) {

            return JsonResult.failOf(e.getMessage());
        }
    }

    @PostMapping("/delete/{groupId}")
    public JsonResult<?> deleteHouseworkInfo(@RequestHeader(name = "Authorization") String authorization,
                                             @PathVariable(name = "groupId") Long groupId,
                                             @RequestParam(name = "date") LocalDate date,
                                             @RequestParam(name = "todoId") Long todoId,
                                             @RequestParam(name = "deleteType") HouseworkInfoDeleteType deleteType) {
        try {
            // 유저 인증 & 그룹원(ACCEPT)인지 권한 확인
            authExternalService.userAuthenticateAndGroupMatch(authorization, groupId);
            houseworkInfoService.deleteHouseworkInfo(
                    HouseworkInfoDeleteRequest.builder()
                            .groupId(groupId)
                            .date(date)
                            .todoId(todoId)
                            .deleteType(deleteType)
                            .build());

            return JsonResult.successOf("Housework Delete Success.");
        } catch (HeachiException e) {
            return JsonResult.failOf(e.getMessage());
        }

    }

    @GetMapping("/update/{groupId}")
    public JsonResult<?> updateHouseworkInfo(@RequestHeader(name = "Authorization") String authorization,
                                             @PathVariable(name = "groupId") Long groupId,
                                             @RequestParam(name = "todoId") Long todoId) {
        try {
            // 유저 인증 & 그룹원(ACCEPT)인지 권한 확인
            authExternalService.userAuthenticateAndGroupMatch(authorization, groupId);

            return JsonResult.successOf(houseworkInfoService.updateHouseworkPage(todoId));
        } catch (HeachiException e) {
            return JsonResult.failOf(e.getMessage());
        }
    }

    @PostMapping("/update/{groupId}")
    public JsonResult<?> updateHouseworkInfo(@RequestHeader(name = "Authorization") String authorization,
                                             @PathVariable(name = "groupId") Long groupId,
                                             @RequestParam(name = "date") LocalDate date,
                                             @RequestParam(name = "todoId") Long todoId,
                                             @Valid @RequestBody HouseworkInfoCreateRequest request) {
        try {
            // 유저 인증 & 그룹원(ACCEPT)인지 권한 확인
            authExternalService.userAuthenticateAndGroupMatch(authorization, groupId);
            houseworkInfoService.updateHousework(HouseworkInfoUpdateServiceRequest.of(request, groupId, todoId, date));

            return JsonResult.successOf("Housework Update Success.");
        } catch (HeachiException e) {
            return JsonResult.failOf(e.getMessage());
        }
    }
}
