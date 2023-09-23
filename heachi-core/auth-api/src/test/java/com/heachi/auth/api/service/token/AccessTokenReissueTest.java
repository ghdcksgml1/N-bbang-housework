package com.heachi.auth.api.service.token;

import com.heachi.auth.api.service.auth.AuthService;
import com.heachi.auth.api.service.jwt.JwtService;
import com.heachi.auth.api.service.oauth.OAuthService;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.mysql.define.user.repository.UserRepository;
import com.heachi.redis.define.refreshToken.RefreshToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class AccessTokenReissueTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Test
    @DisplayName("AccessToken 만료시 재발급 테스트")
    void redisRefreshTokenGenerate() throws Exception {
        // given
        User user = User.builder()
                .name("김민수")
                .role(UserRole.USER)
                .email("kimminsu@dankook.ac.kr")
                .profileImageUrl("https://google.com")
                .build();
        User savedUser = userRepository.save(user);

        HashMap<String, String> map = new HashMap<>();
        map.put("role", savedUser.getRole().name());
        map.put("name", savedUser.getName());
        map.put("profileImageUrl", savedUser.getProfileImageUrl());

        String expiredAccessToken = jwtService.generateExpiredAccessToken(map, savedUser);
        String refreshToken = jwtService.generateRefreshToken(map, savedUser);

        refreshTokenService.saveRefreshToken(RefreshToken.builder().refreshToken(refreshToken).email(user.getEmail()).build());

        // when
        mockMvc.perform(get("/auth/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + expiredAccessToken + " " + refreshToken))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(200))
                .andExpect(jsonPath("$.resObj.role").value("USER"))
                .andExpect(jsonPath("$.resObj.name").value("김민수"))
                .andExpect(jsonPath("$.resObj.email").value("kimminsu@dankook.ac.kr"))
                .andExpect(jsonPath("$.resObj.profileImageUrl").value("https://google.com"));
    }
}
