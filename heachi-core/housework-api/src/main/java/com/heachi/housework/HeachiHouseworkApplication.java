package com.heachi.housework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.heachi", exclude = SecurityAutoConfiguration.class)
public class HeachiHouseworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeachiHouseworkApplication.class, args);
    }
}
