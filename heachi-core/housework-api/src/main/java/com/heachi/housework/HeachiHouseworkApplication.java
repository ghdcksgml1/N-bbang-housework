package com.heachi.housework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.heachi", exclude = SecurityAutoConfiguration.class)
@EnableJpaAuditing // JPA Auditing 기능 활성화 - BaseEntity
public class HeachiHouseworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeachiHouseworkApplication.class, args);
    }
}
