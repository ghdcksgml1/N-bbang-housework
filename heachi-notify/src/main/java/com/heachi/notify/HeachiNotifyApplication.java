package com.heachi.notify;

import com.heachi.admin.common.utils.DateDistance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class HeachiNotifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeachiNotifyApplication.class, args);
    }
}
