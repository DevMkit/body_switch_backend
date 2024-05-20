package kr.co.softhubglobal.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${app.openapi.server-url}")
    private String serverUrl;
    public static final String BEARER_KEY_SECURITY_SCHEME = "bearer-key";

    @Bean
    public OpenAPI AdminApplicationAPI() {

        Server server = new Server();
        server.setUrl(serverUrl);
        server.setDescription("Server URL");

        Info info = new Info()
                .title("Body Switch Admin API")
                .version("1.0")
                .description("Body Switch Admin APIs built by Spring Boot 3.2.5");

        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        BEARER_KEY_SECURITY_SCHEME,
                                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
                )
                .info(info)
                .servers(List.of(server));
    }
}

