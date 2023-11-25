package com.heachi.housework.api.controller.store.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@ToString
@AllArgsConstructor
public class ItemAddControllerRequest {
    @NotEmpty
    private String name;            // (필수) 상품 이름

    private String content;         // (선택) 상품 설명

    @NotNull
    private int price;              // (필수) 상품 가격

    @NotNull
    private MultipartFile file;     // (필수) 사진
}
