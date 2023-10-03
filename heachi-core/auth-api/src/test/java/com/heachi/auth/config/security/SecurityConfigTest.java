package com.heachi.auth.config.security;

import com.heachi.auth.TestConfig;
import com.heachi.auth.api.service.jwt.JwtService;
import com.heachi.auth.config.filter.JwtAuthenticationFilter;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.mysql.define.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest extends TestConfig {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtAuthenticationFilter filter;

    @MockBean
    MockHttpServletRequest request;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("인증받지 않은 사용자는 접근할 수 없다.")
    void unAuthUserTest() throws Exception {
        // given
        String uri = "/unauth";

        // when
        mockMvc.perform(
                        get(uri)
                )
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("인증받은 사용자는 접근할 수 있다. 인증받지 않은 사용자의 접근 가능 URI는 \"/auth/**\"이다.")
    void authUserTest() throws Exception {
        // given
        String uri = "/auth/info";

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

        // when
        String accessToken = jwtService.generateAccessToken(map, savedUser);
        String refreshToken = jwtService.generateRefreshToken(map, savedUser);

        mockMvc.perform(
                        get(uri)
                                .header("Authorization", "Bearer " + accessToken + " " + refreshToken)
                )
                // then
                .andExpect(status().isOk());
    }
}