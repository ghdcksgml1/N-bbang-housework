package com.heachi.housework.api.service.store.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemSelectResponse {
    private String name;
    private String content;
    private int price;
    private String file;

    @Builder
    public ItemSelectResponse(String name, String content, int price, String file) {
        this.name = name;
        this.content = content;
        this.price = price;
        this.file = file;
    }
}
