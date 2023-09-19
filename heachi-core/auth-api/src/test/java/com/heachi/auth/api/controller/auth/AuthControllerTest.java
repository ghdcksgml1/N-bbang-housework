package com.heachi.auth.api.controller.auth;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.oauth.OAuthException;
import com.heachi.auth.TestConfig;
import com.heachi.auth.api.controller.auth.request.AuthRegisterRequest;
import com.heachi.auth.api.service.auth.AuthService;
import com.heachi.auth.api.service.auth.request.AuthServiceRegisterRequest;
import com.heachi.auth.api.service.jwt.JwtService;
import com.heachi.auth.api.service.oauth.OAuthService;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.mysql.define.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.HashMap;
import static com.heachi.mysql.define.user.constant.UserPlatformType.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest extends TestConfig {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OAuthService oAuthService;

    @MockBean
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("카카오 로그인 성공 테스트")
    void loginSuccessTest() throws Exception {
        // given
        UserPlatformType platformType = KAKAO;
        String code = "abcdefg";

        OAuthResponse oAuthResponse = OAuthResponse.builder()
                .platformId("12345")
                .platformType(platformType)
                .email("kms@kakao.com")
                .name("김민수")
                .profileImageUrl("google.com")
                .build();
        when(oAuthService.login(any(UserPlatformType.class), any(String.class), any(String.class))).thenReturn(oAuthResponse); // 성공

        // when
        mockMvc.perform(
                        get("/auth/KAKAO/login?code=" + code)
                )
                // then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("카카오 로그인 실패 테스트")
    void loginFailTest() throws Exception {
        // given
        UserPlatformType platformType = KAKAO;
        String code = "abcdefg";

        when(oAuthService.login(any(UserPlatformType.class), any(String.class), any(String.class))).thenThrow(RuntimeException.class); // 실패

        // when
        mockMvc.perform(
                        get("/auth/KAKAO/login")
                )
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(400));
    }

    @Test
    @DisplayName("카카오 로그인시 State 값이 redis의 키로 존재하지 않으면, LoginStateException 예외가 발생한다.")
    void kakaoLoginFailWhenInvalidState() throws Exception {
        String code = "code";
        String state = UUID.randomUUID().toString();

        // invalidState 값을 사용해 login을 시도하면 Exception 발생함
        given(oAuthService.login(KAKAO, code, state))
                .willThrow(new OAuthException(ExceptionMessage.OAUTH_INVALID_STATE));


        // when
        mockMvc.perform(
                        get("/auth/KAKAO/login")
                                .param("code", code)
                                .param("state", state))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(400))
                .andExpect(jsonPath("$.resMsg").value(ExceptionMessage.LOGINSTATE_NOT_FOUND.getText()))
                .andDo(print());
    }

    @Test
    @DisplayName("네이버 로그인 성공 테스트")
    void naverLoginSuccessTest() throws Exception {
        // given
        UserPlatformType platformType = NAVER;
        String code = "xxx";

        OAuthResponse oAuthResponse = OAuthResponse.builder()
                .platformId("1")
                .platformType(platformType)
                .email("hgd@naver.com")
                .name("홍길동")
                .profileImageUrl("naver.com")
                .build();

        when(oAuthService.login(any(UserPlatformType.class), any(String.class), any(String.class)))
                .thenReturn(oAuthResponse);

        // when
        mockMvc.perform(
                        get("/auth/NAVER/login?code=" + code)
                )
                // then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("네이버 로그인 실패 테스트")
    void naverLoginFailTest() throws Exception {
        // given
        when(oAuthService.login(any(UserPlatformType.class), any(String.class), any(String.class)))
                .thenThrow(RuntimeException.class);

        // when
        mockMvc.perform(
                        get("/auth/NAVER/login")
                )
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(400));
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void registerSuccessTest() throws Exception {
        // given
        // 유효성 검사 통과하는 request
        AuthServiceRegisterRequest request = AuthServiceRegisterRequest.builder()
                .role(UserRole.USER)
                .email("testUser@example.com")
                .phoneNumber("01012341234")
                .build();

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonMapper.builder().build().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(200))
                .andExpect(jsonPath("$.resMsg").value("OK"));
    }

    @Test
    @DisplayName("회원가입 실패 테스트")
    void registerFailTest() throws Exception {
        // given
        // 유효성 검사 실패하는 request
        AuthRegisterRequest request = AuthRegisterRequest.builder()
                .role(UserRole.USER) // userRole null
                .email("1-203-102-3") // 잘못된 형식의 email
                .phoneNumber("01012341234")
                .build();

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonMapper.builder().build().writeValueAsString(request)))
                // .andExpect(status().isBadRequest());
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(400))
                .andExpect(jsonPath("$.resMsg").value("email: must be a well-formed email address"));
    }

    @Test
    @DisplayName("userSimpleInfo 테스트, role, name, email, profileImageUrl이 리턴된다.")
    void userSimpleInfoResponseTest() throws Exception {
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
        String token = jwtService.generateToken(map, savedUser);

        // when
        mockMvc.perform(get("/auth/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(200))
                .andExpect(jsonPath("$.resObj.role").value("USER"))
                .andExpect(jsonPath("$.resObj.name").value("김민수"))
                .andExpect(jsonPath("$.resObj.email").value("kimminsu@dankook.ac.kr"))
                .andExpect(jsonPath("$.resObj.profileImageUrl").value("https://google.com"));
    }

    @Test
    @DisplayName("userSimpleInfo 실패 테스트, 잘못된 Token을 넣으면 오류를 뱉는다.")
    void userSimpleInfoTestWhenInvalidToken() throws Exception {
        // given
        String token = "strangeToken";

        // when
        mockMvc.perform(get("/auth/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(400));
    }
}