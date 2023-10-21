package com.heachi.mysql.define.housework.info.repository;

import com.heachi.mysql.define.housework.info.HouseworkInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HouseworkInfoRepository extends JpaRepository<HouseworkInfo, Long>, HouseworkInfoRepositoryCustom {
    Optional<HouseworkInfo> findByTitle(String title);
}
