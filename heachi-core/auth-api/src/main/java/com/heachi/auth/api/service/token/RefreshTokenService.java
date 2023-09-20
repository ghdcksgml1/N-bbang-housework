package com.heachi.auth.api.service.token;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.refreshToken.RefreshTokenException;
import com.heachi.auth.api.service.jwt.JwtService;
import com.heachi.auth.api.service.jwt.JwtTokenDTO;
import com.heachi.redis.define.refreshToken.RefreshToken;
import com.heachi.redis.define.refreshToken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

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
            throw new RefreshTokenException(ExceptionMessage.JWT_INVALID_RTK);
        }

        // redis에서 rtk 삭제
        RefreshToken rtk = refreshTokenRepository.findById(refreshToken).orElseThrow(() -> {
            throw new RefreshTokenException(ExceptionMessage.JWT_NOT_EXIST_RTK);
        });
        refreshTokenRepository.delete(rtk);
    }

    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
        log.info(">>>> Refresh Token register : {}", refreshToken);
    }

//    public String findByKey(String key) {
//        String email = jwtService.extractUsername(key);
//        String rtk = refreshTokenRepository.findByEmail(email);
//        if (rtk.isEmpty()) {
//            return "false";
//        }
//
//        return rtk;
//    }

//    public String reissueATK(String refreshToken) {
//        Claims claims = jwtService.extractAllClaims(refreshToken);
//
//        // redis에 존재하는 토큰인지 확인
//        if (!jwtService.isTokenValid(refreshToken, claims.getSubject())) {
//            throw new RefreshTokenException(ExceptionMessage.JWT_INVALID_RTK);
//        }
//
//        if (refreshTokenRepository.findByEmail(claims.getSubject()).equals(refreshToken)) {
//            userRepository.findByEmail(claims.getSubject()).;
//
//            jwtService.generateAccessToken(claims, )
//        }
//    }


    /*
    * private RedisTemplate<String, String> redisTemplate;

    public RefreshTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(final RefreshToken refreshToken) {
        redisTemplate.opsForValue().set(
                refreshToken.getEmail(),
                refreshToken.getRefreshToken()
        );
        log.info(">>>> Generated RefreshToken : {}", refreshToken.getEmail());
    }

    public void blackList(final String accessToken) {
        redisTemplate.opsForValue().set(
                accessToken,
                "logout",
                Duration.ofMillis(86400000)
        );
    }

    public String findByEmail(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    public void deleteByEmail(final String email) {
        redisTemplate.delete(email);
    }
    * */
}
