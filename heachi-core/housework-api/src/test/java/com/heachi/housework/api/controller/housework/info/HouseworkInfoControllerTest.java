package com.heachi.housework.api.controller.housework.info;

import com.heachi.admin.common.response.JsonResult;
import com.heachi.external.clients.auth.AuthClients;
import com.heachi.external.clients.auth.response.UserInfoResponse;
import com.heachi.housework.TestConfig;
import com.heachi.housework.api.controller.housework.info.request.HouseworkInfoCreateRequest;
import com.heachi.housework.api.service.auth.AuthExternalService;
import com.heachi.housework.api.service.housework.info.HouseworkInfoService;
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoCreateServiceRequest;
import com.heachi.mysql.define.group.info.GroupInfo;
import com.heachi.mysql.define.group.member.GroupMember;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.info.HouseworkInfo;
import com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType;
import com.heachi.mysql.define.housework.info.repository.HouseworkInfoRepository;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@AutoConfigureMockMvc
@SpringBootTest
public class HouseworkInfoControllerTest extends TestConfig {

    @MockBean
    private GroupMemberRepository groupMemberRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private HouseworkInfoRepository houseworkInfoRepository;

    @MockBean
    private HouseworkInfoService houseworkInfoService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        houseworkInfoRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("Auth 서버 요청 테스트")
    void authExternalRequest() {
        // given
        String token = "token";

        AuthExternalServiceMock authMock = new AuthExternalServiceMock(new AuthClientMock(), groupMemberRepository);

        UserInfoResponse userInfoResponse = authMock.userAuthenticate(token);
        System.out.println("userInfoResponse = " + userInfoResponse);
    }

    private static class AuthClientMock implements AuthClients {
        @Override
        public Mono<JsonResult<UserInfoResponse>> getUserInfo(String headers) {
            return null;
        }
    }

    private static class AuthExternalServiceMock extends AuthExternalService {

        public AuthExternalServiceMock(AuthClients authClients, GroupMemberRepository groupMemberRepository) {
            super(authClients, groupMemberRepository);
        }

        @Override
        public UserInfoResponse userAuthenticate(String authorization) {
            return UserInfoResponse.builder()
                    .email("test@naver.com")
                    .role("testRole")
                    .name("testName")
                    .profileImageUrl("testUrl")
                    .build();
        }

        @Override
        public UserInfoResponse userAuthenticateAndGroupMatch(String authorization, Long groupId) {
            return UserInfoResponse.builder()
                    .email("test@naver.com")
                    .role("testRole")
                    .name("testName")
                    .profileImageUrl("testUrl")
                    .build();
        }

    }

}
