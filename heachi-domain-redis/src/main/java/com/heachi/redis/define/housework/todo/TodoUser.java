package com.heachi.redis.define.housework.todo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TodoUser {

    private String name;
    private String email;
    private String profileImageUrl;

    @Builder
    private TodoUser(String name, String email, String profileImageUrl) {
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }
}