package pe.gob.pj.prueba.infraestructure.doc;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

  @Bean
  OpenAPI apiInfo() {
    return new OpenAPI()
        .info(new Info()
            .title("Base Api Rest")
            .version("1.0.0"));
  }

  @Bean
  GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("public")
        .pathsToMatch("/prueba-api/**")
        .build();
  }

}
