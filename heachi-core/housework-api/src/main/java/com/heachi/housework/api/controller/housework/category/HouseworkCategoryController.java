package com.heachi.housework.api.controller.housework.category;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.housework.api.controller.group.member.response.GroupMemberResponse;
import com.heachi.housework.api.service.housework.category.HouseworkCategoryService;
import com.heachi.housework.api.service.housework.category.response.HouseworkCategoryResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/housework/category")
public class HouseworkCategoryController {

    private final HouseworkCategoryService houseworkCategoryService;

    @ApiResponse(responseCode = "200", description = "카테고리 리스트 조회 성공", content = @Content(schema = @Schema(implementation = HouseworkCategoryResponse.class, type = "array")))
    @GetMapping("/")
    public JsonResult<?> selectCategory() {

        return JsonResult.successOf(houseworkCategoryService.selectCategory());
    }
}
