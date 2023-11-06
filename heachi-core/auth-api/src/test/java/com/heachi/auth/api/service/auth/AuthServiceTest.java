package com.heachi.auth.api.service.auth;

import com.heachi.admin.common.exception.auth.AuthException;
import com.heachi.admin.common.exception.oauth.OAuthException;
import com.heachi.auth.TestConfig;
import com.heachi.auth.api.service.auth.request.AuthServiceRegisterRequest;
import com.heachi.auth.api.service.auth.response.AuthServiceLoginResponse;
import com.heachi.auth.api.service.jwt.JwtService;
import com.heachi.auth.api.service.oauth.OAuthService;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.mysql.define.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static com.heachi.mysql.define.user.constant.UserPlatformType.KAKAO;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthServiceTest extends TestConfig {

    @MockBean
    private OAuthService oAuthService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("유저 플랫폼과 코드를 넘기면, 로그인이 성공하고, JWT 토큰이 정상적으로 발행된다.")
    void loginSuccess() throws Exception {
        // given
        UserPlatformType platformType = UserPlatformType.KAKAO;
        String code = "임의의 코드입니다.";
        String state = "임의의 state입니다.";
        String email = "kimminsu@dankook.ac.kr";

        User user = User.builder()
                .name("김민수")
                .email(email)
                .role(UserRole.USER)
                .build();
        userRepository.save(user);

        OAuthResponse oAuthResponse = OAuthResponse.builder()
                .platformId("123")
                .email(email)
                .name("김민수")
                .profileImageUrl("google.com")
                .build();

        // when
        when(oAuthService.login(any(UserPlatformType.class), any(String.class), any(String.class))).thenReturn(oAuthResponse); // Mocking

        AuthServiceLoginResponse login = authService.login(platformType, code, state);     // 로그인 프로세스
        User findUser = userRepository.findByEmail(email).get();                  // 로그인한 사용자 찾기
        boolean tokenValid = jwtService.isTokenValid(login.getAccessToken(), findUser.getUsername());   // 발행한 토큰 검증

        // then
        assertThat(tokenValid).isTrue();
        assertThat(findUser)
                .extracting("name", "email", "profileImageUrl", "role")
                .contains("김민수", email, "google.com", UserRole.USER);
    }

    @Test
    @DisplayName("로그인이 완료된 사용자의 jwt 토큰에는 알맞은 Claims가 들어가있어야 한다.")
    void loginTokenValidClaims() {
        // given
        UserPlatformType platformType = UserPlatformType.KAKAO;

        User user1 = User.builder()
                .name("김민수")
                .email("abc1")
                .role(UserRole.USER)
                .build();
        User user2 = User.builder()
                .name("김민목")
                .email("abc2")
                .role(UserRole.USER)
                .build();
        User user3 = User.builder()
                .name("김민금")
                .email("abc3")
                .role(UserRole.USER)
                .build();
        userRepository.saveAll(List.of(user1, user2, user3));

        OAuthResponse oAuthResponse1 = OAuthResponse.builder()
                .platformId("111")
                .email("abc1")
                .name("김민수")
                .profileImageUrl("google.com")
                .build();
        OAuthResponse oAuthResponse2 = OAuthResponse.builder()
                .platformId("222")
                .email("abc2")
                .name("김민목")
                .profileImageUrl("google.com")
                .build();
        OAuthResponse oAuthResponse3 = OAuthResponse.builder()
                .platformId("333")
                .email("abc3")
                .name("김민금")
                .profileImageUrl("google.com")
                .build();

        // when
        when(oAuthService.login(any(UserPlatformType.class), eq("abc1"), eq("abc1"))).thenReturn(oAuthResponse1); // Mocking
        when(oAuthService.login(any(UserPlatformType.class), eq("abc2"), eq("abc2"))).thenReturn(oAuthResponse2); // Mocking
        when(oAuthService.login(any(UserPlatformType.class), eq("abc3"), eq("abc3"))).thenReturn(oAuthResponse3); // Mocking

        AuthServiceLoginResponse abc1 = authService.login(platformType, "abc1", "abc1");        // 김민수
        AuthServiceLoginResponse abc2 = authService.login(platformType, "abc2", "abc2");        // 김민목
        AuthServiceLoginResponse abc3 = authService.login(platformType, "abc3", "abc3");        // 김민금

        Claims claims1 = jwtService.extractAllClaims(abc1.getAccessToken());
        Claims claims2 = jwtService.extractAllClaims(abc2.getAccessToken());
        Claims claims3 = jwtService.extractAllClaims(abc3.getAccessToken());

        // then
        assertAll(
                // 김민수
                () -> assertThat(claims1.get("role")).isEqualTo("USER"),
                () -> assertThat(claims1.get("name")).isEqualTo("김민수"),
                () -> assertThat(claims1.get("profileImageUrl")).isEqualTo("google.com"),
                // 김민목
                () -> assertThat(claims2.get("role")).isEqualTo("USER"),
                () -> assertThat(claims2.get("name")).isEqualTo("김민목"),
                () -> assertThat(claims2.get("profileImageUrl")).isEqualTo("google.com"),
                // 김민금
                () -> assertThat(claims3.get("role")).isEqualTo("USER"),
                () -> assertThat(claims3.get("name")).isEqualTo("김민금"),
                () -> assertThat(claims3.get("profileImageUrl")).isEqualTo("google.com")
        );
    }

    @Test
    @DisplayName("기존 사용자의 정보가 바뀌면 수정되어야한다. (별명, 프로필 사진)")
    void loginUpdateUserInfoWhenUserInfoChanged() {
        // given
        UserPlatformType platformType = KAKAO;
        User user = User.builder()
                .platformId("12345")
                .platformType(platformType)
                .email("kms@kakao.com")
                .name("김민수")
                .profileImageUrl("google.co.kr")
                .role(UserRole.USER)
                .build();
        userRepository.save(user);
        OAuthResponse oAuthResponse = OAuthResponse.builder()
                .platformId("12345")
                .platformType(platformType)
                .email("kms@kakao.com")
                .name("김민수짱")
                .profileImageUrl("google.com")
                .build();
        when(oAuthService.login(any(UserPlatformType.class), any(String.class), any(String.class))).thenReturn(oAuthResponse);

        // when
        authService.login(platformType, "123344", "state");
        User findUser = userRepository.findByEmail("kms@kakao.com").get();

        // then
        assertAll(() -> {
            assertThat(findUser.getName()).isEqualTo("김민수짱");
            assertThat(findUser.getProfileImageUrl()).isEqualTo("google.com");
        });
    }

    @Test
    @DisplayName("UNAUTH 미가입자 회원가입 성공 테스트")
    public void registerUnauthUserSuccessTest() {
        UserPlatformType platformType =KAKAO;
        String email = "user@example.com";
        String platformId = "qwer1234@";
        String name = "tesrUser";
        String phoneNumber = "01234567890";
        String profileImageUrl = "https://example.com/profile.jpg";

        // UNAUTH 사용자 저장
        User unauthUser = User.builder()
                .platformId(platformId)
                .platformType(platformType)
                .role(UserRole.UNAUTH)
                .email(email)
                .name(name)
                .profileImageUrl(profileImageUrl)
                .build();

        User findUser = userRepository.save(unauthUser);

        // 회원가입 요청 생성 (CENTER)
        AuthServiceRegisterRequest request = AuthServiceRegisterRequest.builder()
                .role(UserRole.USER)
                .phoneNumber(phoneNumber)
                .email(email)
                .build();

        // when
        AuthServiceLoginResponse response = authService.register(request);

        User savedUser = userRepository.findByEmail(request.getEmail()).get();
        boolean tokenValid = jwtService.isTokenValid(response.getAccessToken(), savedUser.getUsername());   // 발행한 토큰 검증


        // then
        assertEquals(UserRole.USER, response.getRole());
        assertThat(tokenValid).isTrue();
    }

    @Test
    @DisplayName("UNAUTH 미가입자 회원가입 실패 테스트")
    public void registerUnauthUserFailTest() {
        UserPlatformType platformType = KAKAO;
        String email = "user@example.com";
        String platformId = "qwer1234@";
        String name = "tesrUser";
        String phoneNumber = "01234567890";
        String profileImageUrl = "https://example.com/profile.jpg";

        User user = User.builder()
                .platformId(platformId)
                .platformType(platformType)
                .role(UserRole.USER)
                .email(email)
                .name(name)
                .profileImageUrl(profileImageUrl)
                .build();

        userRepository.save(user);

        // 회원가입 요청 생성 (CENTER)
        AuthServiceRegisterRequest request = AuthServiceRegisterRequest.builder()
                .role(UserRole.USER)
                .phoneNumber(phoneNumber)
                .email(email)
                .build();

        // when

        // then
        assertThrows(RuntimeException.class, () -> {
            authService.register(request);
        });
    }

    @Test
    @DisplayName("존재하지 않는 Email로 계정삭제를 진행할 수 없다.")
    void isNotProcessingWhenEmailIsNotExist() {
        // given
        String invalidEmail = "abcdd@abc.com";

        // when
        assertThrows(AuthException.class,
                () -> authService.userDelete(invalidEmail));
    }

    @Test
    @DisplayName("존재하는 계정의 Email로 계정삭제를 진행할 수 있다.")
    void successProcessingWhenEmailIsExistInDB() {
        // given
        User user = User.builder()
                .platformId("12345")
                .platformType(KAKAO)
                .email("kms@kakao.com")
                .name("김민수")
                .profileImageUrl("google.co.kr")
                .role(UserRole.USER)
                .build();
        userRepository.save(user);

        // when
        authService.userDelete("kms@kakao.com");
        User deletedUser = userRepository.findByEmail("kms@kakao.com").get();

        // then
        assertThat(deletedUser.getRole()).isEqualTo(UserRole.WITHDRAW);
    }
}