package com.heachi.housework.api.service.housework.todo.response;

import com.heachi.mysql.define.user.User;
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

    public static TodoUser of(User user) {

        return TodoUser.builder()
                .name(user.getName())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
