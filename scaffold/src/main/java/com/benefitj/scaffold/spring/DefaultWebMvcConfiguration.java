package com.benefitj.scaffold.spring;

import com.benefitj.scaffold.page.PageBodyArgumentResolver;
import com.benefitj.spring.mvc.EnableCustomArgumentResolverWebMvcConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分页参数解析
 */
@EnableCustomArgumentResolverWebMvcConfigurer
@Configuration
public class DefaultWebMvcConfiguration {

  /**
   * 请求分页
   */
  @ConditionalOnMissingBean
  @Bean
  public PageBodyArgumentResolver pageBodyArgumentResolver() {
    return new PageBodyArgumentResolver();
  }

}
