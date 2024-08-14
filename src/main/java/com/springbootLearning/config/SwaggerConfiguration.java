package com.springbootLearning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI springDocOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("基站辐射检测接口")
                        .description("基站辐射检测项目相关接口")
                        .version("1.0.0")
                        .license(
                            new License().name("项目测试环境首页")
                                .url("http://localhost:8080/swagger-ui/index.html")
                        )
                    );
    }
}
