package com.lucifer.electronics.store.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import jdk.jfr.Name;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//  Way - 2: Implementing Spring Security in Swagger
//  Adding a security scheme of type HTTP with bearer token authentication to the components.
@SecurityScheme(
        name = "schemer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
//  Setting the metadata information for the API, including title, description, version, contact information, license information and external documentation for an API.
@OpenAPIDefinition(
        info = @Info(
                title = "TheElectronicsStore Application",
                description = "This is TheElectronicsStore project APIs developed By VK",
                version = "1.0",
                contact = @Contact(
                    name = "IamVk009",
                    email = ""
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://github.com/IamVk009/TheElectronicsStore"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "TheElectronicsStore Application",
                url = "https://github.com/IamVk009/TheElectronicsStore"
        )
)
public class SwaggerConfig {
//  Way - 1: Implementing Spring Security in Swagger
/**
     * This method configures an OpenAPI object with security schemes and metadata.
     * @return Configured OpenAPI object
     */
/*
    @Bean
    public OpenAPI openAPI(){

        String schemeName = "bearerScheme";

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
*/
}
