package com.heachi.housework.api.controller.housework.category;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.housework.api.service.housework.category.HouseworkCategoryService;
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

    @GetMapping("/")
    public JsonResult<?> selectCategory() {

        return JsonResult.successOf(houseworkCategoryService.selectCategory());
    }
}
