package com.heachi.mysql.define.housework.info.repository;

import com.heachi.mysql.TestConfig;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.category.repository.HouseworkCategoryRepository;
import com.heachi.mysql.define.housework.info.HouseworkInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HouseworkInfoRepositoryTest extends TestConfig {

    @Autowired
    private HouseworkInfoRepository houseworkInfoRepository;

    @Autowired
    private HouseworkCategoryRepository houseworkCategoryRepository;

    @AfterEach
    void tearDown() {
        houseworkInfoRepository.deleteAllInBatch();
        houseworkCategoryRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("HOUSEWORK_INFO 저장 테스트")
    void houseworkInfoSaveTest() {
        // given
        HouseworkCategory houseworkCategory = generateHouseworkCategory();
        houseworkCategoryRepository.save(houseworkCategory);

        HouseworkInfo houseworkInfo = generateHouseworkInfo(houseworkCategory);

        // when
        HouseworkInfo save = houseworkInfoRepository.save(houseworkInfo);

        //then
        assertThat(save.getTitle()).isEqualTo("빨래");
        assertThat(save.getDetail()).isEqualTo("빨래 돌리기");
    }
}