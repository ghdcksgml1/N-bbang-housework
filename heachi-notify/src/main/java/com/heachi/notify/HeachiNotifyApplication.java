package com.heachi.notify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.heachi")
public class HeachiNotifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeachiNotifyApplication.class, args);
    }
}
