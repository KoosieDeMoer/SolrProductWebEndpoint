package io.swagger.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
    info = @Info(title = "Swagger Server", version = "1.0.0", description = "Product search API",
        termsOfService = "", contact = @Contact(email = "you@your-company.com"),
        license = @License(name = "", url = "http://unlicense.org")))
public class Bootstrap {
}
