package com.heachi.housework.api.controller.housework.info;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.housework.api.controller.housework.info.request.HouseworkInfoAddRequest;
import com.heachi.housework.api.controller.housework.info.response.HouseworkInfoAddResponse;
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

    @PostMapping("/add/{groupId}")
    public JsonResult<HouseworkInfoAddResponse> houseworkAdd(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable(name = "groupId") Long groupId,
            @RequestBody HouseworkInfoAddRequest request
    ) {
//        List<String> tokens = Arrays.asList(token.split(" "));
//
//        if (tokens.size() == 3) {
//            HouseworkAddResponseDTO addResponse = houseworkService.houseworkAdd(tokens.get(2), HouseworkServiceAddRequestDTO.of(request));
//
//            return JsonResult.successOf(addResponse);
//        } else {
//            log.warn(">>>> Invalid Header Access : {}", ExceptionMessage.JWT_INVALID_HEADER.getText());
//            return JsonResult.failOf(ExceptionMessage.JWT_INVALID_HEADER.getText());
//        }

        HouseworkInfoAddResponse addResponse = houseworkInfoService.houseworkAdd(token, groupId, HouseworkInfoAddServiceRequest.of(request));

        return JsonResult.successOf(addResponse);
    }
}
