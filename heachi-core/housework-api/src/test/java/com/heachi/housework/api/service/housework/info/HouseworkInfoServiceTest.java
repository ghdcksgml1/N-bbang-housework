package com.heachi.housework.api.service.housework.info;

import com.heachi.admin.common.exception.housework.HouseworkException;
import com.heachi.external.clients.auth.response.UserInfoResponse;
import com.heachi.housework.TestConfig;
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoCreateServiceRequest;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType;
import com.heachi.mysql.define.housework.info.repository.HouseworkInfoRepository;
import com.heachi.mysql.define.housework.member.HouseworkMember;
import com.heachi.mysql.define.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class HouseworkInfoServiceTest extends TestConfig {
    @MockBean
    private HouseworkInfoService houseworkInfoService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private HouseworkInfoRepository houseworkInfoRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        houseworkInfoRepository.deleteAllInBatch();
    }

   /* @Test
    @DisplayName("집안일 추가 성공 테스트")
    void houseworkAddServiceSuccessTest() throws Exception {
        // given
        when(houseworkInfoService.createHouseworkInfo(any(UserInfoResponse.class), any(HouseworkInfoCreateServiceRequest.class)))

        HouseworkInfoAddResponse addResponseDTO = houseworkInfoService.createHouseworkInfo(UserInfoResponse.builder().build(), HouseworkInfoCreateServiceRequest.builder().build());

        assertEquals(response, addResponseDTO);
    }

    @Test
    @DisplayName("집안일 추가 실패 테스트")
    void houseworkAddServiceFailTest() throws Exception {
        // when
        when(houseworkInfoService.createHouseworkInfo(any(UserInfoResponse.class), any(HouseworkInfoCreateServiceRequest.class)))
                .thenThrow(HouseworkException.class);
        // then
        assertThrows(HouseworkException.class, () -> {
            houseworkInfoService.createHouseworkInfo(UserInfoResponse.builder().build(), HouseworkInfoCreateServiceRequest.builder().build());
        });
    }*/
}