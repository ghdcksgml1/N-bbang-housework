package com.heachi.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.heachi")
@EnableJpaAuditing // JPA Auditing 기능 활성화 - BaseEntity
public class HeachiAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeachiAuthApplication.class, args);
    }
}
