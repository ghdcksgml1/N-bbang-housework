package com.heachi.mysql.define.housework.category.repository;

import com.heachi.mysql.TestConfig;
import com.heachi.mysql.define.group.info.repository.GroupInfoRepository;
import com.heachi.mysql.define.group.member.repository.GroupMemberRepository;
import com.heachi.mysql.define.housework.category.HouseworkCategory;
import com.heachi.mysql.define.housework.info.repository.HouseworkInfoRepository;
import com.heachi.mysql.define.housework.member.repository.HouseworkMemberRepository;
import com.heachi.mysql.define.housework.todo.repository.HouseworkTodoRepository;
import com.heachi.mysql.define.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HouseworkCategoryRepositoryTest extends TestConfig {

    @Autowired
    private HouseworkCategoryRepository houseworkCategoryRepository;

    @AfterEach
    void tearDown() {
        houseworkCategoryRepository.deleteAllInBatch();
    }


    @Test
    @DisplayName("카테고리 이름으로 조회 성공 테스트")
    void test1() {
        // given
        HouseworkCategory category = houseworkCategoryRepository.save(generateHouseworkCategory());

        // when & then
        assertThat(houseworkCategoryRepository.findHouseworkCategoryByName(category.getName()).equals("집안일"));
    }
}