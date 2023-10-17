package com.heachi.housework.api.controller.housework.info;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.housework.HouseworkException;
import com.heachi.housework.TestConfig;
import com.heachi.housework.api.controller.housework.info.request.HouseworkInfoAddRequest;
import com.heachi.housework.api.controller.housework.info.response.HouseworkInfoAddResponse;
import com.heachi.housework.api.service.housework.info.HouseworkInfoService;
import com.heachi.housework.api.service.housework.info.request.HouseworkInfoAddServiceRequest;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType;
import com.heachi.mysql.define.housework.info.repository.HouseworkInfoRepository;
import com.heachi.mysql.define.housework.member.HouseworkMember;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class HouseworkInfoControllerTest extends TestConfig {
    @Autowired
    private MockMvc mockMvc;

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

    @Test
    @DisplayName("집안일 추가 성공 테스트")
    void houseworkAddSuccessTest() throws Exception {
        // given
        String token = "token";

        HouseworkInfoAddResponse response = generateHouseworkAddResponse();

        when(houseworkInfoService.houseworkAdd(any(String.class), any(Long.class), any(HouseworkInfoAddServiceRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/housework/add/1")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonMapper.builder().build().writeValueAsString(HouseworkInfoAddRequest.builder().build())))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(200))
                .andDo(print());

    }

    @Test
    @DisplayName("집안일 추가 실패 테스트")
    void houseworkAddFailTest() throws Exception {
        // given
        String token = "token";

        HouseworkInfoAddResponse response = generateHouseworkAddResponse();

        when(houseworkInfoService.houseworkAdd(any(String.class), any(Long.class), any(HouseworkInfoAddServiceRequest.class)))
                .thenThrow(new HouseworkException(ExceptionMessage.HOUSEWORK_ADD_FAIL));

        mockMvc.perform(
                        post("/housework/add/1")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonMapper.builder().build().writeValueAsString(HouseworkInfoAddRequest.builder().build())))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(400))
                .andExpect(jsonPath("$.resMsg").value("집안일 추가에 실패했습니다."))
                .andDo(print());

    }

    public static HouseworkInfoAddResponse generateHouseworkAddResponse() {
        List<HouseworkMember> houseworkMembers = new ArrayList<>();
        houseworkMembers.add(HouseworkMember.builder().build());

        return HouseworkInfoAddResponse.builder()
                .houseworkMembers(houseworkMembers)
                .houseworkCategory(HouseworkCategory.builder().build())
                .title("title")
                .detail("detail")
                .type(HouseworkPeriodType.HOUSEWORK_PERIOD_WEEK)
                .dayDate(null)
                .weekDate("0")
                .monthDate(null)
                .endTime(null)
                .build();
    }

}