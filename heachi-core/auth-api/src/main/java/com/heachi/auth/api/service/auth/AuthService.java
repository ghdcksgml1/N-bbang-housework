package com.heachi.auth.api.service.auth;

import com.heachi.auth.api.service.auth.request.AuthServiceRegisterRequest;
import com.heachi.auth.api.service.auth.request.AuthServiceLoginRequest;
import com.heachi.auth.api.service.auth.response.AuthServiceLoginResponse;
import com.heachi.mysql.define.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public AuthServiceLoginResponse login(AuthServiceLoginRequest request) {

        return null;
    }

    public AuthServiceLoginResponse register(AuthServiceRegisterRequest request) {

        return null;
    }
}
