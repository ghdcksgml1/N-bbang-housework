package com.heachi.housework.api.controller.housework.info;

import com.heachi.housework.TestConfig;
import com.heachi.housework.api.service.housework.info.HouseworkInfoService;
import com.heachi.mysql.define.housework.info.repository.HouseworkInfoRepository;
import com.heachi.mysql.define.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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

    /*@Test
    @DisplayName("집안일 추가 성공 테스트")
    void houseworkAddSuccessTest() throws Exception {
        // given
        String token = "token";

        HouseworkInfoAddResponse response = generateHouseworkAddResponse();

        when(houseworkInfoService.houseworkAdd(any(UserInfoResponse.class), any(HouseworkInfoAddServiceRequest.class)))
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
        Long groupId = 1L;
        HouseworkInfoAddResponse response = generateHouseworkAddResponse();

        when(houseworkInfoService.houseworkAdd(any(UserInfoResponse.class), any(HouseworkInfoAddServiceRequest.class)))
                .thenThrow(new HouseworkException(ExceptionMessage.HOUSEWORK_ADD_FAIL));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/housework/add/" + groupId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapper.builder().build().writeValueAsString(HouseworkInfoAddRequest.builder().build())))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resCode").value(400))
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
    }*/

}