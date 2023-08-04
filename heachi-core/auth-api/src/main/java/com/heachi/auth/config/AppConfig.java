package com.heachi.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heachi.mysql.define.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserRepository userRepository;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetailsService userDetailsService = ((username) -> userRepository.findByEmail(username).orElseThrow(
                () -> {
                    throw new UsernameNotFoundException("Username을 찾을 수 없습니다.");
                }
        ));
        return userDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());

        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
