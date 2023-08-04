package com.heachi.admin.common.api;

import com.heachi.admin.common.response.JsonResult;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


class HeachiHttpTemplateImplTest {
    ApplicationContext ac = new AnnotationConfigApplicationContext(WebClientConfig.class, HeachiHttpTemplateImpl.class);

    @Test
    void test() throws Exception{
        // given
        HeachiHttpTemplateImpl bean = ac.getBean(HeachiHttpTemplateImpl.class);

        // when
        JsonResult jsonResult = bean.get("http://localhost:8001");

        // then
        System.out.println(jsonResult.toString());
    }
}