package com.heachi.auth.api.service.token;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.jwt.JwtException;
import com.heachi.auth.api.service.jwt.JwtService;
import com.heachi.auth.api.service.jwt.JwtTokenDTO;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.redis.define.refreshToken.RefreshToken;
import com.heachi.redis.define.refreshToken.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    final RefreshTokenRepository refreshTokenRepository;
    final JwtService jwtService;

    public void logout(String refreshToken) {

        String email = jwtService.extractAllClaims(refreshToken).getSubject();

        // refreshToken 유효성 검사
        if (!jwtService.isTokenValid(refreshToken, email)) {
            throw new JwtException(ExceptionMessage.JWT_INVALID_RTK);
        }

        // redis에서 rtk 삭제
        RefreshToken rtk = refreshTokenRepository.findById(refreshToken).orElseThrow(() -> {
            throw new JwtException(ExceptionMessage.JWT_NOT_EXIST_RTK);
        });
        refreshTokenRepository.delete(rtk);
    }

    public void saveRefreshToken(RefreshToken refreshToken) {
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        log.info(">>>> Refresh Token register : {}", savedToken);
    }

    public String reissue(Claims claims, String refreshToken) {
        // 레디스에 존재하는지 확인
        if (refreshTokenRepository.findById(refreshToken).isEmpty()) {
            throw new JwtException(ExceptionMessage.JWT_NOT_EXIST_RTK);
        }

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