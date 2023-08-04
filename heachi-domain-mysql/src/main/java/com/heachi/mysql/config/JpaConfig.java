package com.heachi.mysql.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.heachi.mysql.define")
@EnableJpaRepositories(basePackages = {"com.heachi.mysql.define"})
public class JpaConfig {

}
