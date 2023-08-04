package com.heachi.auth.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heachi.admin.common.exception.jwt.JwtExceptionMsg;
import com.heachi.auth.TestConfig;
import com.heachi.auth.api.service.hello.HelloQueryRepository;
import com.heachi.auth.api.service.jwt.JwtService;
import com.heachi.auth.config.filter.JwtAuthenticationFilter;
import com.heachi.mysql.define.hello.repository.HelloRepository;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.mysql.define.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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
        String uri = "/auth/";

        User user = User.builder()
                .name("김민수")
                .email("kimminsu@dankook.ac.kr")
                .role(UserRole.USER)
                .build();
        User savedUser = userRepository.save(user);

        // when
        String token = jwtService.generateToken(savedUser);
        mockMvc.perform(
                        get(uri)
                                .header("Authorization", "Bearer " + token)
                )
                // then
                .andExpect(status().isOk());
    }
}