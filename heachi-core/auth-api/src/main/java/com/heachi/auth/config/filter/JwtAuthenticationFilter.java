package com.heachi.auth.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.jwt.JwtException;
import com.heachi.admin.common.response.JsonResult;
import com.heachi.auth.api.service.jwt.JwtService;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserRole;
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

            // token의 claims에 userEmail이 있는지 체크, 토큰이 유효한지 체크
            if (userEmail != null && jwtService.isTokenValid(token, userEmail)) {
                Claims claims = jwtService.extractAllClaims(token);
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
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("utf-8");
        response.setCharacterEncoding(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(JsonResult.failOf(message.getText())));
    }
}
