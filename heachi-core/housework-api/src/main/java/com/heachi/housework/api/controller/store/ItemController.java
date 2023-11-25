package com.heachi.housework.api.controller.store;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.clients.auth.response.UserInfoResponse;
import com.heachi.housework.api.controller.store.request.ItemAddControllerRequest;
import com.heachi.housework.api.service.auth.AuthExternalService;
import com.heachi.housework.api.service.store.ItemService;
import com.heachi.housework.api.service.store.request.ItemAddServiceRequest;
import com.heachi.s3.api.service.AwsS3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final AuthExternalService authExternalService;
    private final ItemService itemService;
    private final AwsS3Service awsS3Service;

    @PostMapping("/add")
    public JsonResult<?> addItem(@RequestHeader(name = "Authorization") String authorization,
                                 @Valid @ModelAttribute(name = "itemAddRequest") ItemAddControllerRequest request) {

        UserInfoResponse user = authExternalService.userAuthenticate(authorization);
        String uploadedImageURL = awsS3Service.uploadImage(request.getFile());
        itemService.addItem(ItemAddServiceRequest.builder()
                .email(user.getEmail())
                .file(uploadedImageURL)
                .price(request.getPrice())
                .name(request.getName())
                .content(request.getContent())
                .build());

        return JsonResult.successOf("상품 등록에 성공하였습니다.");
    }

    @GetMapping("/")
    public JsonResult<?> selectItemList(@RequestHeader(name = "Authorization") String authorization) {
        authExternalService.userAuthenticate(authorization);

        return JsonResult.successOf(itemService.selectItemList());
    }

    @GetMapping("/delete/{itemId}")
    public JsonResult<?> deleteItem(@RequestHeader(name = "Authorization") String authorization,
                                    @PathVariable(name = "itemId") Long itemId) {
        UserInfoResponse user = authExternalService.userAuthenticate(authorization);
        itemService.deleteItem(itemId, user.getEmail());

        return JsonResult.successOf("상품 등록 취소에 성공하였습니다.");
    }

    @GetMapping("/{itemId}")
    public JsonResult<?> selectItem(@RequestHeader(name = "Authorization") String authorization,
                                    @PathVariable(name = "itemId") Long itemId) {
        authExternalService.userAuthenticate(authorization);
        return JsonResult.successOf(itemService.selectItem(itemId));
    }
}
