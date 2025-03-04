package com.example.choose_one.config.security;

import com.example.choose_one.exceptionHandler.AccessDeniedHandler;
import com.example.choose_one.filter.JwtAuthenticationFilter;
import com.example.choose_one.model.customuser.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AccessDeniedHandler accessDeniedHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .authorizeHttpRequests(it -> {
                    try {
                        it.requestMatchers("/swagger-ui.index.html",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/actuator",
                                        "/actuator/**",
                                        "/user/signup",
                                        "/user/login",
                                        "/token/reissue").permitAll()
                                .requestMatchers("/post/delete/{id}").hasAuthority("ROLE_ADMIN")
                                .anyRequest().hasAuthority("ROLE_USER")
                                .and()
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .exceptionHandling(config ->
                                        config.accessDeniedHandler(accessDeniedHandler));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        return httpSecurity.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // CorsConfigurationSource 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://3.35.8.131:8080");
        configuration.addAllowedOrigin("http://localhost:8080");// 허용할 출처
        configuration.addAllowedMethod("*"); // 허용할 HTTP method
        configuration.addAllowedHeader("*"); // 허용할 헤더
        configuration.setAllowCredentials(true); // 쿠키 인증을 허용

        // UrlBasedCorsConfigurationSource 사용 (spring-web의 클래스)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 요청에 대해 CORS 설정
        return source;
    }
}
