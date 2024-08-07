package com.shortlink.webapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Nikita Lyashkevich",
                        email = "liroy2468@gmail.com"
                ),
                description = "Open Api documentation for the Short Link Service, which allows you" +
                              " to create short links from the original long urls and use them anywhere.",
                title = "RESTful API for the Short Link Service (Course Work)",
                version = "1.0"),

        security = {
                @SecurityRequirement(name = "jwt"),
                @SecurityRequirement(name = "oauth2")
        }
)
@SecurityScheme(
        name = "jwt",
        description = "JWT bearer auth",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@SecurityScheme(
        name = "oauth2",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "http://localhost:8080/oauth2/authorization/google",
                        tokenUrl = "https://www.googleapis.com/oauth2/v4/token"
                )
        )
)
//@SecurityScheme(
//        name = "oauth2",
//        type = SecuritySchemeType.OAUTH2,
//        flows = @OAuthFlows(
//                authorizationCode = @OAuthFlow(
//                        authorizationUrl = "http://localhost:8080/oauth2/authorization/google",
//                        tokenUrl = "https://www.googleapis.com/oauth2/v4/token"
//                )
//        )
//)
public class SwaggerConfig {}
