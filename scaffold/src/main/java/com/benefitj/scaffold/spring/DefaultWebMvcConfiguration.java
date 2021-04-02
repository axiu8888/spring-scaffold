package com.benefitj.scaffold.spring;

import com.benefitj.scaffold.page.PageBodyArgumentResolver;
import com.benefitj.spring.mvc.EnableCustomArgumentResolverWebMvcConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 分页参数解析
 */
@ConditionalOnMissingBean(DefaultWebMvcConfiguration.class)
@EnableCustomArgumentResolverWebMvcConfigurer
@Configuration
public class DefaultWebMvcConfiguration implements WebMvcConfigurer {

  /**
   * 请求分页
   */
  @ConditionalOnMissingBean
  @Bean
  public PageBodyArgumentResolver pageBodyArgumentResolver() {
    return new PageBodyArgumentResolver();
  }

  /**
   * 页面跨域访问Controller过滤
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowCredentials(false)
        .allowedOriginPatterns("*")
        .exposedHeaders("*")
        .allowedHeaders("*")
        .maxAge(36000L)
        .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        .allowedOrigins("*");
  }

}
