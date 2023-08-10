package com.heachi.auth.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.jwt.JwtException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.auth.api.service.jwt.JwtService;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(">>>> [Jwt Authentication Filter] <<<<");

        try {
            String authHeader = request.getHeader("Authorization");
            String token;
            String userEmail;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return ;
            }

            token = authHeader.substring(7);
            userEmail = jwtService.extractUsername(token);

            // token의 claims에 userEmail이 있는데, 인증정보가 없는 경우 -> 토큰 생성해주기
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail); // 유저 이메일 있는지 확인

                if (jwtService.isTokenValid(token, userDetails)) {
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
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e){
            logger.error("Could not set user authentication in security context {}" , e);
            jwtExceptionHandler(response, ExceptionMessage.JWT_TOKEN_EXPIRED);
        }catch (UnsupportedJwtException e){
            logger.error("Could not set user authentication in security context {}" , e);
            jwtExceptionHandler(response, ExceptionMessage.JWT_UNSUPPORTED);
        }catch (MalformedJwtException e){
            logger.error("Could not set user authentication in security context {}" , e);
            jwtExceptionHandler(response, ExceptionMessage.JWT_MALFORMED);
        }catch (SignatureException e){
            logger.error("Could not set user authentication in security context {}" , e);
            jwtExceptionHandler(response, ExceptionMessage.JWT_SIGNATURE);
        }catch (IllegalArgumentException e){
            logger.error("Could not set user authentication in security context {}" , e);
            jwtExceptionHandler(response, ExceptionMessage.JWT_ILLEGAL_ARGUMENT);
        }
    }

    private void jwtExceptionHandler(HttpServletResponse response, ExceptionMessage message) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setCharacterEncoding("utf-8");
        response.setCharacterEncoding(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(JsonResult.failOf(message.getText())));
    }
}
