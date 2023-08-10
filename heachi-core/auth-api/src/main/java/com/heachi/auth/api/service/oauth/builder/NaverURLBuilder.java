package com.heachi.auth.api.service.oauth.builder;

import org.springframework.stereotype.Component;

@Component
public class NaverURLBuilder implements OAuthURLBuilder {

    @Override
    public String authorize(String state) {
        return null;
    }

    @Override
    public String token(String code) {
        return null;
    }

    @Override
    public String profile() {
        return null;
    }
}
