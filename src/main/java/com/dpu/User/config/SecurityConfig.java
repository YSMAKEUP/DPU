package com.dpu.User.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 테스트를 위해 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/signup", "/css/**", "/js/**", "/images/**").permitAll() // 명시적 허용
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form.disable()); // 커스텀 로그인 페이지 사용 시 disable 유지 가능

        return http.build();
    }
}