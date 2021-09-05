package com.benefitj.scaffold;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 接口文档配置
 */
@ConditionalOnExpression("'${swagger.enable}' == 'true'") // 是否打开swagger
@EnableOpenApi
//@EnableSwagger2
public abstract class SwaggerConfig {


  @Bean
  public Docket docket() {
    return createRestApi();
  }

  /**
   * 包路径: com.benefitj.module.controller
   */
  public abstract String basePackage();

  /**
   * ApiInfo
   */
  public abstract ApiInfo apiInfo();

  public Docket createRestApi() {
    return new Docket(DocumentationType.OAS_30)
        .useDefaultResponseMessages(false)
        .forCodeGeneration(true)
        .select()
        .apis(RequestHandlerSelectors.basePackage(basePackage()))
        .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo());
  }

}

