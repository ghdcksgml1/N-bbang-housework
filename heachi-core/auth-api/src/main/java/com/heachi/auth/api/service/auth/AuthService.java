package com.heachi.auth.api.service.auth;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.auth.AuthException;
import com.heachi.admin.common.exception.jwt.JwtException;
import com.heachi.auth.api.controller.token.response.ReissueAccessTokenResponse;
import com.heachi.auth.api.service.auth.request.AuthServiceRegisterRequest;
import com.heachi.auth.api.service.auth.response.AuthServiceLoginResponse;
import com.heachi.auth.api.service.jwt.JwtService;
import com.heachi.auth.api.service.jwt.JwtTokenDTO;
import com.heachi.auth.api.service.oauth.OAuthService;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.auth.api.service.token.RefreshTokenService;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.mysql.define.user.repository.UserRepository;
import com.heachi.redis.define.refreshToken.RefreshToken;
import com.heachi.redis.define.refreshToken.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OAuthService oAuthService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

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

        // JWT Access Token, Refresh Token 발급
        JwtTokenDTO tokens = createJwtToken(findUser);

        return AuthServiceLoginResponse.builder()
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .role(findUser.getRole())
                .build();
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.logout(refreshToken);
    }

    @Transactional
    public AuthServiceLoginResponse register(AuthServiceRegisterRequest request) {
        User findUser = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
            // UNAUTH인 토큰을 받고 회원 탈퇴 후 그 토큰으로 회원가입 요청시 예외 처리
            log.warn(">>>> User Not Exist : {}", ExceptionMessage.AUTH_INVALID_REGISTER.getText());
            throw new AuthException(ExceptionMessage.AUTH_INVALID_REGISTER);
        });

        // UNAUTH 토큰으로 회원가입을 요청했지만 이미 update되어 UNAUTH가 아닌 사용자 예외 처리
        if (findUser.getRole() != UserRole.UNAUTH) {
            log.warn(">>>> Not UNAUTH User : {}", ExceptionMessage.AUTH_DUPLICATE_UNAUTH_REGISTER.getText());
            throw new AuthException(ExceptionMessage.AUTH_DUPLICATE_UNAUTH_REGISTER);
        }

        // 회원가입 정보 DB 반영
        findUser.updateRegister(request.getRole(), request.getPhoneNumber());

        // JWT Access Token, Refresh Token 재발급
        JwtTokenDTO tokens = createJwtToken(findUser);

        return AuthServiceLoginResponse.builder()
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .role(findUser.getRole())
                .build();
    }

    private JwtTokenDTO createJwtToken(User user) {
        // JWT 토큰 생성을 위한 claims 생성
        HashMap<String, String> claims = new HashMap<>();
        claims.put(ROLE_CLAIM, user.getRole().name());
        claims.put(NAME_CLAIM, user.getName());
        claims.put(PROFILE_IMAGE_CLAIM, user.getProfileImageUrl());

        // Access Token 생성
        final String accessToken = jwtService.generateAccessToken(claims, user);
        // Refresh Token 생성
        final String refreshToken = jwtService.generateRefreshToken(claims, user);

        log.info(">>>> {} generate Tokens", user.getName());

        // Refresh Token 저장 - REDIS
        RefreshToken rt = RefreshToken.builder()
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .build();
        refreshTokenService.saveRefreshToken(rt);


        return JwtTokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void userDelete(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.warn(">>>> User Delete Fail : {}", ExceptionMessage.AUTH_NOT_FOUND.getText());
            throw new AuthException(ExceptionMessage.AUTH_NOT_FOUND);
        });

        try {
            userRepository.deleteById(user.getId());
            log.info(">>>> {} Info is Deleted.", user.getName());
        } catch (IllegalArgumentException e) {
            log.error(">>>> ID = {} : 계정 삭제에 실패했습니다.", user.getId());
            throw new AuthException(ExceptionMessage.AUTH_DELETE_FAIL);
        }
    }

    @Transactional
    public ReissueAccessTokenResponse reissueAccessToken(String refreshToken) {
        Claims claims = jwtService.extractAllClaims(refreshToken);

        // 토큰 검증
        if (jwtService.isTokenValid(refreshToken, claims.getSubject())) {
            // 리프레시 토큰을 이용해 새로운 엑세스 토큰 발급
            String accessToken = refreshTokenService.reissue(claims, refreshToken);
            log.info(">>>> {} reissue AccessToken.", claims.getSubject());

            return ReissueAccessTokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } else {
            log.warn(">>>> Token Validation Fail : {}", ExceptionMessage.JWT_INVALID_RTK.getText());
            throw new JwtException(ExceptionMessage.JWT_INVALID_RTK);
        }

    }
}