package com.benefitj.scaffold.spring;

import com.benefitj.spring.mvc.EnableCustomArgumentResolverWebMvcConfigurer;
import com.benefitj.spring.security.jwt.EnableJwtSecurityConfiguration;
import org.springframework.context.annotation.Configuration;

@EnableJwtSecurityConfiguration
@EnableCustomArgumentResolverWebMvcConfigurer
@Configuration
public class WebMvcConfig {
}
