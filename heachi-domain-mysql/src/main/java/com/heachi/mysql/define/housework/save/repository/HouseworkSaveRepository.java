package com.heachi.mysql.define.housework.save.repository;

import com.heachi.mysql.define.housework.save.HouseworkSave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseworkSaveRepository extends JpaRepository<HouseworkSave, Long> {
}
