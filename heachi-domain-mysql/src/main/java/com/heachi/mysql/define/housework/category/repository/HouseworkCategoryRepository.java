package com.heachi.mysql.define.housework.category.repository;

import com.heachi.mysql.define.housework.category.HouseworkCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseworkCategoryRepository extends JpaRepository<HouseworkCategory, Long> {
}
