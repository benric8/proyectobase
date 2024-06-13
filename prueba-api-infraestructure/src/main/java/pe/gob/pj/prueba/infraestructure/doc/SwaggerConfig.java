package pe.gob.pj.prueba.infraestructure.doc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SwaggerConfig {
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.OAS_30)
				.select()
				.apis(RequestHandlerSelectors.basePackage("pe.gob.pj.prueba.infraestructure.rest"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}
	
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Consulta Servicios de Pide")
                .description("Servicio que permite consultar canalizar las consultas a los metodos que expone el PIDE de la PCM")
                .version("1.0.0")
                .build();
    }
}
