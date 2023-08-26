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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder passwordEncoder;
    @PersistenceContext
    private EntityManager entityManager;

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

        // JWT 토큰 발급
        final String token = createJwtToken(findUser);

        return AuthServiceLoginResponse.builder()
                .token(token)
                .role(findUser.getRole())
                .build();
    }

    @Transactional
    public AuthServiceLoginResponse register(UserPlatformType platformType, AuthServiceRegisterRequest request) {
        try {
            User findUser = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
                // UNAUTH인 토큰을 받고 회원 탈퇴 후 그 토큰으로 회원가입 요청시 예외 처리
                throw new OAuthException(ExceptionMessage.OAUTH_INVALID_REGISTER);
            });

            // User findUser = userRepository.findByEmail(request.getEmail()).get();

            // 회원가입 정보 DB 반영
            findUser.updateRegister(passwordEncoder.encode(request.getPlatformId()),
                    request.getRole(),
                    request.getName(),
                    request.getEmail(),
                    request.getPhoneNumber(),
                    request.getProfileImageUrl());

            // 수정된 회원정보 조회
            String jpql = "SELECT u FROM USERS u WHERE u.email = :email";
            User updateUser = entityManager.createQuery(jpql, User.class)
                    .setParameter("email", request.getEmail())
                    .getSingleResult();

            // UNAUTH 토큰으로 회원가입을 요청했지만 이미 회원가입시 update되어 UNAUTH가 아닌 사용자 예외 처리
            if (updateUser.getRole() != UserRole.UNAUTH) {
                throw new OAuthException(ExceptionMessage.OAUTH_UNAUTH_DUPLICATE_REGISTER);
            }

            // 바뀐 회원 정보로 JWT 토큰 재발급
            final String token = createJwtToken(updateUser);

            return AuthServiceLoginResponse.builder()
                    .token(token)
                    .role(findUser.getRole())
                    .build();

        } catch (OAuthException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("exception!!!");
        }
    }

    private String createJwtToken(User user) {
        // JWT 토큰 생성을 위한 claims 생성
        HashMap<String, String> claims = new HashMap<>();
        claims.put(ROLE_CLAIM, user.getRole().name());
        claims.put(NAME_CLAIM, user.getName());
        claims.put(PROFILE_IMAGE_CLAIM, user.getProfileImageUrl());

        // JWT 토큰 생성 (claims, UserDetails)
        final String token = jwtService.generateToken(claims, user);

        // 로그인 반환 객체 생성
        return token;
    }
}
