package com.heachi.auth.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.jwt.JwtException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.auth.api.service.jwt.JwtService;
import com.heachi.auth.api.service.token.RefreshTokenService;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.redis.define.refreshToken.RefreshToken;
import com.heachi.redis.define.refreshToken.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(">>>> [Jwt Authentication Filter] <<<<");

        String authHeader = request.getHeader("Authorization");
        String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            List<String> tokens = Arrays.asList(authHeader.split(" "));

            if (tokens.size() == 3) {
                userEmail = jwtService.extractUsername(tokens.get(1));

                // 토큰 유효성 검증 후 시큐리티 등록
                authenticateUser(userEmail, tokens.get(1), request);

                filterChain.doFilter(request, response);
            } else {
                jwtExceptionHandler(response, ExceptionMessage.JWT_INVALID_HEADER);
            }

        } catch (ExpiredJwtException e) {
            logger.error("Could not set user authentication in security context {}", e);

            // 엑세스 토큰 재발급 API인 /auth/reissue로 전달하는 코드
            // 헤더에서 토큰 추출 - 잘못된 헤더면 이미 try문에서 걸러졌을 것
            List<String> tokens = Arrays.asList(authHeader.split(" "));

            // refreshToken이 존재하는지 확인 - TTL로 만료시간 자동 체크
            RefreshToken refreshToken = refreshTokenRepository.findById(tokens.get(2)).orElseThrow(
                    () -> new JwtException(ExceptionMessage.JWT_NOT_EXIST_RTK));

            userEmail = jwtService.extractUsername(refreshToken.getRefreshToken());

            // 엑세스 토큰 재발급
            String reissueAccessToken = refreshTokenService.reissue(jwtService.extractAllClaims(refreshToken.getRefreshToken()), refreshToken.getRefreshToken());

            // 재발급 받은 토큰 유효성 검증 후 시큐리티에 등록
            authenticateUser(userEmail, reissueAccessToken, request);

            filterChain.doFilter(request, response);

        } catch (UnsupportedJwtException e) {
            logger.error("Could not set user authentication in security context {}", e);
            jwtExceptionHandler(response, ExceptionMessage.JWT_UNSUPPORTED);
        } catch (MalformedJwtException e) {
            logger.error("Could not set user authentication in security context {}", e);
            jwtExceptionHandler(response, ExceptionMessage.JWT_MALFORMED);
        } catch (SignatureException e) {
            logger.error("Could not set user authentication in security context {}", e);
            jwtExceptionHandler(response, ExceptionMessage.JWT_SIGNATURE);
        } catch (IllegalArgumentException e) {
            logger.error("Could not set user authentication in security context {}", e);
            jwtExceptionHandler(response, ExceptionMessage.JWT_ILLEGAL_ARGUMENT);
        }
    }

    private void authenticateUser(String userEmail, String accessToken, HttpServletRequest request) {
        if (userEmail != null && jwtService.isTokenValid(accessToken, userEmail)) {
            Claims claims = jwtService.extractAllClaims(accessToken);

            UserDetails userDetails = User.builder()
                    .email(userEmail)
                    .role(UserRole.valueOf(claims.get("role", String.class)))
                    .name(claims.get("name", String.class))
                    .profileImageUrl(claims.get("profileImageUrl", String.class))
                    .build();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // SecurityContext에 Authenticaiton 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    private void jwtExceptionHandler(HttpServletResponse response, ExceptionMessage message) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(JsonResult.failOf(message.getText())));
    }
}
