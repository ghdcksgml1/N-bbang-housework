package com.heachi.auth.api.service.token;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.jwt.JwtException;
import com.heachi.auth.api.service.jwt.JwtService;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.redis.define.refreshToken.RefreshToken;
import com.heachi.redis.define.refreshToken.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    final RefreshTokenRepository refreshTokenRepository;
    final JwtService jwtService;

    // Logout 시 Redis에 저장된 RTK 삭제
    public void logout(String refreshToken) {

        String email = jwtService.extractAllClaims(refreshToken).getSubject();

        RefreshToken rtk = refreshTokenRepository.findById(refreshToken).orElseThrow(() -> {
            log.warn(">>>> Token Not Exist : {}", ExceptionMessage.JWT_NOT_EXIST_RTK.getText());
            throw new JwtException(ExceptionMessage.JWT_NOT_EXIST_RTK);
        });

        // refreshToken 유효성 검사
        if (!jwtService.isTokenValid(refreshToken, rtk.getEmail())) {
            log.warn(">>>> Token Validation Fail : {}", ExceptionMessage.JWT_INVALID_RTK.getText());
            throw new JwtException(ExceptionMessage.JWT_INVALID_RTK);
        }

        refreshTokenRepository.delete(rtk);
        log.info(">>>> {}'s RefreshToken id deleted.", email);
    }

    public void saveRefreshToken(RefreshToken refreshToken) {
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        log.info(">>>> Refresh Token register : {}", savedToken.getRefreshToken());
    }

    public String reissue(Claims claims, String refreshToken) {

        refreshTokenRepository.findById(refreshToken).orElseThrow(() -> {
            log.warn(">>>> Token Not Exist : {}", ExceptionMessage.JWT_NOT_EXIST_RTK.getText());
            throw new JwtException(ExceptionMessage.JWT_NOT_EXIST_RTK);
        });

        String role = claims.get("role", String.class);

        UserDetails userDetails = User.builder()
                .email(jwtService.extractUsername(refreshToken))
                .role(UserRole.valueOf(role))
                .name(claims.get("name", String.class))
                .profileImageUrl(claims.get("profileImageUrl", String.class))
                .build();

        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            map.put(entry.getKey(), entry.getValue().toString());
        }

        return jwtService.generateAccessToken(map, userDetails);
    }
}