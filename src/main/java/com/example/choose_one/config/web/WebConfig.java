package com.example.choose_one.config.web;

import com.example.choose_one.interceptor.AuthorizationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthorizationInterceptor authorizationInterceptor;
    private List<String> SWAGGER = List.of("/swagger-ui/index.html",
            "/swagger-ui/**",
            "/v3/api-docs/**");
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor)
                .excludePathPatterns(SWAGGER)
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/signup")

                ;
    }
}
