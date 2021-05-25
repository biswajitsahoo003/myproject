package com.tcl.dias.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * Config Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Value("${swagger.info.title}")
	private String swaggerTitle;

	@Value("${swagger.info.description}")
	private String swaggerDescription;

	@Value("${swagger.info.version}")
	private String swaggerVersion;

	@Value("${swagger.info.toc}")
	private String swaggerToc;

	/**
	 * 
	 * api - This API is used for configuring the swagger
	 * 
	 * @return Docket
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
				.apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build().apiInfo(apiInfo());
	}

	/**
	 * 
	 * apiInfo - It builds the API info for the swagger
	 * 
	 * @return
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(swaggerTitle).description(swaggerDescription).version(swaggerVersion)
				.termsOfServiceUrl(swaggerToc).build();
	}
}
