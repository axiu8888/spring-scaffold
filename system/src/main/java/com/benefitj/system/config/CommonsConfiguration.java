package com.benefitj.system.config;

import com.benefitj.quartz.spring.EnableQuartzConfuration;
import com.benefitj.scaffold.spring.EnableDruidConfuration;
import com.benefitj.scaffold.spring.EnableScaffoldWebSecurityConfiguration;
import com.benefitj.spring.aop.EnableAutoAopWebHandler;
import com.benefitj.spring.aop.log.EnableRequestLoggingHandler;
import com.benefitj.spring.applicationevent.EnableApplicationListener;
import com.benefitj.spring.security.url.AnnotationUrlRegistryConfigurerCustomizer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.stream.Collectors;

@EntityScan("com.benefitj.system.model")
@MapperScan("com.benefitj.system.mapper")
@EnableAutoAopWebHandler // AOP
@EnableRequestLoggingHandler // 打印请求日志
@EnableApplicationListener // Application Event
@EnableQuartzConfuration // quartz
@EnableDruidConfuration // druid
@EnableScaffoldWebSecurityConfiguration // web security
@Configuration
public class CommonsConfiguration {

  @Bean
  public AnnotationUrlRegistryConfigurerCustomizer annotationUrlAuthorizationConfigurerCustomizer() {
    return new AnnotationUrlRegistryConfigurerCustomizer() {
      @Override
      public void customize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
        System.err.println(getAllMetadata().stream()
            .map(metadata -> String.format("[%s], [%s]",
                StringUtils.join(metadata.getUris(), ", "),
                StringUtils.join(metadata.getMethodTypes(), ", "))
            )
            .collect(Collectors.joining("\n")));
        super.customize(registry);
      }
    };
  }

}
