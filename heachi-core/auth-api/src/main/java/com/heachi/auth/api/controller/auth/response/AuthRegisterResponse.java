package com.heachi.auth.api.controller.auth.response;

import lombok.Getter;

@Getter
public class AuthRegisterResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String profileImageUrl;
    private String createdDateTime;
}
