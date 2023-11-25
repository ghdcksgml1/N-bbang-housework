package com.heachi.mysql.define.housework.category.repository;

import com.heachi.mysql.define.housework.category.HouseworkCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseworkCategoryRepository extends JpaRepository<HouseworkCategory, Long> {
    // 카테고리 이름으로 조회
    public HouseworkCategory findHouseworkCategoryByName(String categoryName);
}
