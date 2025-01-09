package com.example.choose_one.config.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .components(new Components()) // Api 구성 요소들 정의
                .info(apiInfo()); // Api 기본 정보 설정
    }

    private Info apiInfo(){
        return new Info()
                .title("Choose_One Api")
                .description("Choose_One Api 입니다.")
                .version("1.0.p");
    }

    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper){
        return new ModelResolver(objectMapper);
    }
}
