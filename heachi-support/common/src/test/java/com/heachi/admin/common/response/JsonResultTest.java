package com.heachi.admin.common.response;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonResultTest {
    @Test
    @DisplayName("HttpStatus.getReasonPhrase 내용 확인")
    void responseTextContentCheck() {
        // given
        JsonResult jsonResult = JsonResult.failOf();

        // when


        // then
        Assertions.assertThat(jsonResult.getResMsg())
                .isEqualTo("Bad Request");
    }
}