package com.benefitj.system.config;

import com.benefitj.scaffold.quartz.EnableQuartzConfuration;
import com.benefitj.scaffold.spring.EnableDruidConfuration;
import com.benefitj.scaffold.spring.EnableScaffoldWebSecurityConfiguration;
import com.benefitj.spring.aop.log.EnableHttpLoggingHandler;
import com.benefitj.spring.aop.web.EnableAutoAopWebHandler;
import com.benefitj.spring.athenapdf.EnableAthenapdf;
import com.benefitj.spring.eventbus.EnableEventBusPoster;
import com.benefitj.spring.redis.EnableRedisMessageChannel;
import com.benefitj.spring.security.url.AnnotationUrlRegistryConfigurerCustomizer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.stream.Collectors;

@EntityScan("com.benefitj.system.model")
@MapperScan("com.benefitj.system.mapper")
@EnableAthenapdf              // PDF打印
@EnableAutoAopWebHandler      // AOP
@EnableHttpLoggingHandler     // 打印请求日志
@EnableQuartzConfuration      // quartz
@EnableDruidConfuration       // druid
@EnableEventBusPoster         // EventBus
@EnableScaffoldWebSecurityConfiguration // web security
@EnableRedisMessageChannel    // redis
@Configuration
public class CommonsConfiguration {

  @Bean
  public AnnotationUrlRegistryConfigurerCustomizer annotationUrlAuthorizationConfigurerCustomizer() {
    return new AnnotationUrlRegistryConfigurerCustomizer() {

      final Logger log = LoggerFactory.getLogger(getClass());

      @Override
      public void customize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
        // 打印忽略的路径，不包含通用路径
        log.info("\n{}", getAllMetadata().stream()
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
