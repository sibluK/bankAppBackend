package bank.bankApp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Nedas Kulbis",
                        email = "nedas.kulbis@stud.viko.lt",
                        url = "eif.viko.lt"
                ),
                description = "OpenApi documentation for bank application",
                title = "Bank application",
                version = "1.0",
                license = @License(
                        name = "Free to use",
                        url = "eif.viko.lt"
                )
        ),
        servers = {
                @Server(
                        description = "Local Dev",
                        url = "localhost:7171"
                ),
                @Server(
                        description = "Local Test",
                        url = "localhost:7272"
                )
        }
)

public class OpenAPIConfig {

}
