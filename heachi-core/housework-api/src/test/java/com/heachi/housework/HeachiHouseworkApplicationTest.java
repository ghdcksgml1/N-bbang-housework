package com.heachi.housework;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@ConfigurationPropertiesScan
class HeachiHouseworkApplicationTest {

    @Test
    void contextLoads() {
    }


}