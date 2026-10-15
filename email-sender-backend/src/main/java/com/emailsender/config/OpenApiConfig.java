package com.emailsender.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI emailSenderOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Email Sender API")
                        .description("API REST para envío de correos electrónicos usando SendGrid")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Email Sender")
                                .email("soporte@emailsender.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor local")
                ));
    }
}
