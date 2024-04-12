package com.lucifer.electronics.store.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    String schemeName = "bearerScheme";

    /**
     * This method configures an OpenAPI object with security schemes and metadata.
     * @return Configured OpenAPI object
     */
    @Bean
    public OpenAPI openAPI(){
//      Way - 1: Implementing Spring Security in Swagger
//      Creating and configuring an OpenAPI object.
        return new OpenAPI()
//              Adding security of type defined in below component to each and every API endpoint.
                .addSecurityItem(new SecurityRequirement()
                        .addList(schemeName))
//              Adding a security scheme of type HTTP with bearer token authentication to the components.
                .components(new Components()
                        .addSecuritySchemes(schemeName, new SecurityScheme()
                                .name(schemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .bearerFormat("JWT")
                                .scheme("bearer")))
//              Setting the metadata information for the API, including title, description, version, contact information, license information and external documentation for an API.
                .info(new Info().title("TheElectronicsStore API")
                        .description("This is TheElectronicsStore project API developed By VK")
                        .version("1.0")
                        .contact(new Contact().name("IamVk009").email(""))
                        .license(new License().name("Apache 2.0").url("https://github.com/IamVk009/TheElectronicsStore"))
                ).externalDocs(new ExternalDocumentation().description("TheElectronicsStore Application")
                        .url("https://github.com/IamVk009/TheElectronicsStore"));
    }
}
