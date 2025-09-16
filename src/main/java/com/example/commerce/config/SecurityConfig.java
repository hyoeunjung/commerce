package com.example.commerce.config;

import com.example.commerce.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(auth -> auth
                        // 회원가입 / 로그인
                        .requestMatchers(new AntPathRequestMatcher("/users/signup")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/users/signin")).permitAll()

                        // 상품 검색 (누구나 가능)
                        .requestMatchers(new AntPathRequestMatcher("/api/products/search")).permitAll()

                        // 상품 관리 (ADMIN 전용)
                        .requestMatchers(new AntPathRequestMatcher("/api/products/**")).hasRole("ADMIN")

                        // 장바구니 (로그인 사용자만)
                        .requestMatchers(new AntPathRequestMatcher("/api/cart/**")).hasAnyRole("USER", "ADMIN")

                        // H2 콘솔
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()

                        // 나머지는 인증 필요
                        .anyRequest().authenticated()
                );

        // H2 콘솔 frame 깨짐 방지
        http.headers().frameOptions().disable();

        // JWT 필터 등록
        http.addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
