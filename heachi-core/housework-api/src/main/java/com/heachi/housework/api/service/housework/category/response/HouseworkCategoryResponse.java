package com.heachi.housework.api.service.housework.category.response;

import com.heachi.mysql.define.housework.category.HouseworkCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HouseworkCategoryResponse {
    private Long id;        // 카테고리 아이디
    private String name;    // 카테고리 이름

    @Builder
    private HouseworkCategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static HouseworkCategoryResponse of(HouseworkCategory houseworkCategory) {

        return HouseworkCategoryResponse.builder()
                .id(houseworkCategory.getId())
                .name(houseworkCategory.getName())
                .build();
    }
}
