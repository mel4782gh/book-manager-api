package com.techreturners.bookmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
public class BookmanagerApplication {

	public static void main(String[] args) {

		SpringApplication.run(BookmanagerApplication.class, args);


	}
	/* You are using the Builder pattern here to construct the Docket
            instance.
            It is a way of choosing how to build (construct) the Docket object by
            specifying some construction steps.
            The Special Builder object helps with the process of constructing.*/
	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.OAS_30)
				.select()
				.paths(PathSelectors.any())
				.apis(RequestHandlerSelectors.
						withClassAnnotation(RestController.class))
				.build()
				.apiInfo(apiInformation());
	}

	private ApiInfo apiInformation() {
		return new ApiInfoBuilder()
				.title(" Minimalist Book Manager API")
				.description("Need a helping hand to manage your books? This is the API for you! ")
				.version("v1")
				.build();

	}
}
