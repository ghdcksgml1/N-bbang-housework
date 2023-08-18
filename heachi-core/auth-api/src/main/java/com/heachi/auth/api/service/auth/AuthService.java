package com.heachi.auth.api.service.auth;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.oauth.OAuthException;
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

    @Transactional
    public AuthServiceLoginResponse login(UserPlatformType platformType, String code, String state) {
        OAuthResponse loginResponse = oAuthService.login(platformType, code, state);
        log.info(">>>> {}님이 로그인하셨습니다.", loginResponse.getName());

        // OAuth 로그인을 시도한 사용자 정보가 DB에 존재하는지
        // null인 경우 - 영속성 컨텍스트 변경 X. 비어있는 상태
        // null이 아닌 경우 - 해당 사용자 정보 영속성 컨텍스트에 가져와 로드
        User findUser = userRepository.findByEmail(loginResponse.getEmail()).orElse(null);

        // 이미 가입된 경우
        // 영속성 컨텍스트 변경 감지 발동 - 트랜잭션 종료시 자동 반영
        if (findUser != null) {
            findUser.updateProfile(loginResponse.getName(), loginResponse.getProfileImageUrl());
            System.out.println("AuthService.login - 이미 가입된 경우");
        }

        // 가입되지 않은 경우
        // 새로운 사용자 정보를 생성해 영속성 컨텍스트에 저장
        // 트랜잭션 종료시 DB 반영
        if (findUser == null) {
            System.out.println("AuthService.login - 가입되지 않은 경우");
            User saveUser = User.builder()
                    .platformId(loginResponse.getPlatformId())
                    .platformType(loginResponse.getPlatformType())
                    .role(UserRole.USER)
                    .name(loginResponse.getName())
                    .email(loginResponse.getEmail())
                    .profileImageUrl(loginResponse.getProfileImageUrl())
                    .build();

            userRepository.save(saveUser);
        }

        // 인증이 완료되지 않은 사용자(UserRole = UNAUTH) 거르기
        if (findUser.getRole().name().equals(UserRole.UNAUTH.name())) {
            throw new OAuthException(ExceptionMessage.OAUTH_UNAUTH_USER);
        }

        // JWT 토큰 생성을 위한 claims 생성
        HashMap<String, String> claims = new HashMap<>();
        claims.put(ROLE_CLAIM, findUser.getRole().name());
        claims.put(NAME_CLAIM, findUser.getName());
        claims.put(PROFILE_IMAGE_CLAIM, findUser.getProfileImageUrl());

        // JWT 토큰 생성 (claims, UserDetails)
        final String token = jwtService.generateToken(claims, findUser);

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
