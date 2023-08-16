package com.heachi.auth.api.service.auth;

import com.heachi.auth.api.service.auth.request.AuthServiceRegisterRequest;
import com.heachi.auth.api.service.auth.response.AuthServiceLoginResponse;
import com.heachi.auth.api.service.jwt.JwtService;
import com.heachi.auth.api.service.oauth.OAuthService;
import com.heachi.auth.api.service.oauth.request.OAuthRegisterRequest;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.mysql.define.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OAuthService oAuthService;
    private final JwtService jwtService;


    private final String ROLE_CLAIM = "role";
    private final String NAME_CLAIM = "name";
    private final String PROFILE_IMAGE_CLAIM = "profileImageUrl";

    public AuthServiceLoginResponse login(UserPlatformType platformType, String code, String state) {
        OAuthResponse loginResponse = oAuthService.login(platformType, code, state);
        log.info(">>>> {}님이 로그인하셨습니다.", loginResponse.getName());

        // loginResponse 정보를 이용해 DB에서 찾아보고 없으면 등록해준다.
        User findUser = userRepository.findByEmail(loginResponse.getEmail())
                .orElseGet(() -> {
                    User saveUser = User.builder()
                            .platformId(loginResponse.getPlatformId())
                            .platformType(loginResponse.getPlatformType())
                            .role(UserRole.USER)
                            .name(loginResponse.getName())
                            .email(loginResponse.getEmail())
                            .profileImageUrl(loginResponse.getProfileImageUrl())
                            .build();

                    return userRepository.save(saveUser);
                });

        // JWT 토큰 생성을 위한 claims 생성
        HashMap<String, String> claims = new HashMap<>();
        claims.put(ROLE_CLAIM, findUser.getRole().name());
        claims.put(NAME_CLAIM, findUser.getName());
        claims.put(PROFILE_IMAGE_CLAIM, findUser.getProfileImageUrl());

        // JWT 토큰 생성 (claims, UserDetails)
        String token = jwtService.generateToken(claims, findUser);

        // 로그인 반환 객체 생성
        AuthServiceLoginResponse authServiceLoginResponse = AuthServiceLoginResponse.builder()
                .token(token)
                .build();

        return authServiceLoginResponse;
    }

    public AuthServiceLoginResponse register(UserPlatformType platformType, AuthServiceRegisterRequest request) {
        return null;
    }
}
