package com.heachi.housework.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(@Value("${heachiCoreAuthUrls}") String[] serverList) {
        Info info = new Info()
                .title("N빵 집안일 도메인 API Document")
                .version("0.1")
                .description("N빵 집안일 도메인 API 문서입니다.\n" +
                        "해당 문서를 이용해 N빵 집안일 REST API를 테스트해볼 수 있습니다.")
                .contact(new Contact()
                        .name("📍 N-bbang-housework Backend GitHub Link")
                        .url("https://github.com/ghdcksgml1/N-bbang-housework"))
                .license(new License()
                        .name("⚖️ Apache License Version 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0"));

        List<Server> servers = Arrays.stream(serverList)
                .map((url) -> new Server().url(url))
                .collect(Collectors.toList());

        SecurityScheme securityScheme = new SecurityScheme()
                .name("Bearer Authentication")
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("Bearer");
        SecurityRequirement schemaRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(schemaRequirement))
                .info(info)
                .servers(servers);
    }
}
