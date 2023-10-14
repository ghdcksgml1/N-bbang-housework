package com.heachi.housework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
@RequiredArgsConstructor
public class AppConfig implements WebMvcConfigurer {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (activeProfile.equals("prod")) {
            prodProfileCorsMapping(registry);
        } else {
            devProfileCorsMapping(registry);
        }
    }

    // 개발환경에서는 Cors 모두 오픈
    private void devProfileCorsMapping(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET","POST","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    // 프로덕션 환경에서는 Cors 설정을 Front 페이지와 허용할 서버만 등록해준다.
    private void prodProfileCorsMapping(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET","POST","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
