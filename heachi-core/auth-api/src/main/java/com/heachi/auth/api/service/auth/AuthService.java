package com.heachi.auth.api.service.auth;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.oauth.OAuthException;
import com.heachi.auth.api.controller.auth.response.AuthRegisterResponse;
import com.heachi.auth.api.service.auth.request.AuthServiceRegisterRequest;
import com.heachi.auth.api.service.auth.response.AuthServiceLoginResponse;
import com.heachi.auth.api.service.jwt.JwtService;
import com.heachi.auth.api.service.oauth.OAuthService;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.mysql.define.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OAuthService oAuthService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final String ROLE_CLAIM = "role";
    private static final String NAME_CLAIM = "name";
    private static final String PROFILE_IMAGE_CLAIM = "profileImageUrl";

    @Transactional
    public AuthServiceLoginResponse login(UserPlatformType platformType, String code, String state) {
        OAuthResponse loginResponse = oAuthService.login(platformType, code, state);
        log.info(">>>> {}님이 로그인하셨습니다.", loginResponse.getName());

        // OAuth 로그인을 시도한 사용자 정보가 DB에 존재하는지 확인 후 없다면 등록
        User findUser = userRepository.findByEmail(loginResponse.getEmail())
                .orElseGet(() -> {
                    User saveUser = User.builder()
                            .platformId(loginResponse.getPlatformId())
                            .platformType(loginResponse.getPlatformType())
                            .role(UserRole.UNAUTH)
                            .name(loginResponse.getName())
                            .email(loginResponse.getEmail())
                            .profileImageUrl(loginResponse.getProfileImageUrl())
                            .build();

                    return userRepository.save(saveUser);
                });

        // 기존 회원의 경우 name, profileImageUrl 변하면 update
        findUser.updateProfile(loginResponse.getName(), loginResponse.getProfileImageUrl());

        // JWT 토큰 생성을 위한 claims 생성
        HashMap<String, String> claims = new HashMap<>();
        claims.put(ROLE_CLAIM, findUser.getRole().name());
        claims.put(NAME_CLAIM, findUser.getName());
        claims.put(PROFILE_IMAGE_CLAIM, findUser.getProfileImageUrl());

        // JWT 토큰 생성 (claims, UserDetails)
        final String token = jwtService.generateToken(claims, findUser);

        // 로그인 반환 객체 생성
        return AuthServiceLoginResponse.builder()
                .token(token)
                .role(findUser.getRole())
                .build();
    }

    // 회원가입 후 바로 로그인된 상태가 아닌 다시 로그인 시도하도록
    // 반환 타입을 AuthServiceLoginResponse에서 AuthRegisterResponse로 바꿔봤어요
    public AuthRegisterResponse register(UserPlatformType platformType, AuthServiceRegisterRequest request) {

        User saveUser = User.builder()
                .platformId(passwordEncoder.encode(request.getPlatformId()))
                .platformType(platformType)
                .role(request.getRole())
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .profileImageUrl(request.getProfileImageUrl())
                .build();

        User savedUser = userRepository.save(saveUser);

        AuthRegisterResponse registerResponse = AuthRegisterResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .phoneNumber(savedUser.getPhoneNumber())
                .profileImageUrl(savedUser.getProfileImageUrl())
                .createdDateTime(savedUser.getCreatedDateTime().toString())
                .build();

        return registerResponse;
    }
}
