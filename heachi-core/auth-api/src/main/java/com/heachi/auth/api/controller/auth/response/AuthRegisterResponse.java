package com.heachi.auth.api.controller.auth.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AuthRegisterResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String profileImageUrl;
    private String createdDateTime;
}
