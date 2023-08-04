package com.heachi.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.heachi")
public class HeachiAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeachiAuthApplication.class, args);
    }

}
