package com.benefitj.scaffold.common;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 接口文档配置
 */
@EnableSwagger2
@ConditionalOnExpression("'${swagger.enable}' == 'true'") // 是否打开swagger
public abstract class SwaggerConfig {


  @Bean
  public Docket docket() {
    return createRestApi();
  }

  /**
   * 包路径: com.benefitj.module.controller
   */
  public abstract String[] basePackages();

  /**
   * ApiInfo
   */
  public abstract ApiInfo apiInfo();

  public Docket createRestApi() {
    ApiSelectorBuilder select = new Docket(DocumentationType.SWAGGER_2)
        .useDefaultResponseMessages(false)
        .forCodeGeneration(true)
        .select();
    String[] basePackages = basePackages();
    for (String basePackage : basePackages) {
      select.apis(RequestHandlerSelectors.basePackage(basePackage));
    }
    return select.paths(PathSelectors.any()) // 匹配
        .build()
        .apiInfo(apiInfo());
  }
}

