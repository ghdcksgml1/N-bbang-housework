package com.heachi.housework.api.service.store.request;

import com.heachi.mysql.define.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemAddServiceRequest {
    private String email;
    private String name;
    private String content;
    private int price;
    private String file;

    @Builder
    public ItemAddServiceRequest(String email, String name, String content, int price, String file) {
        this.email = email;
        this.name = name;
        this.content = content;
        this.price = price;
        this.file = file;
    }
}
