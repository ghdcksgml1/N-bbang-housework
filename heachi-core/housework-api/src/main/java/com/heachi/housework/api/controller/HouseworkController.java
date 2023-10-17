package com.heachi.housework.api.controller;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.housework.api.controller.request.HouseworkAddRequestDTO;
import com.heachi.housework.api.controller.response.HouseworkAddResponseDTO;
import com.heachi.housework.api.service.HouseworkService;
import com.heachi.housework.api.service.request.HouseworkServiceAddRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/housework")
@RestController
public class HouseworkController {
    private final HouseworkService houseworkService;

    @PostMapping("/add/{groupId}")
    public JsonResult<HouseworkAddResponseDTO> houseworkAdd(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable(name = "groupId") Long groupId,
            @RequestBody HouseworkAddRequestDTO request
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

        HouseworkAddResponseDTO addResponse = houseworkService.houseworkAdd(token, groupId, HouseworkServiceAddRequestDTO.of(request));

        return JsonResult.successOf(addResponse);
    }
}
