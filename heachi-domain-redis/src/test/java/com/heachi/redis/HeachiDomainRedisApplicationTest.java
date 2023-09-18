package com.heachi.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class HeachiDomainRedisApplicationTest {

    public static void main(String[] args) {
        SpringApplication.run(HeachiDomainRedisApplicationTest.class, args);
    }
}
