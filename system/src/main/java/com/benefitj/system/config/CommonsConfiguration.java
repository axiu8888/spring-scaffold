package com.benefitj.system.config;

import com.benefitj.event.EventBusPoster;
import com.benefitj.quartz.spring.EnableQuartzConfuration;
import com.benefitj.scaffold.spring.EnableDruidConfuration;
import com.benefitj.scaffold.spring.EnableScaffoldWebSecurityConfiguration;
import com.benefitj.spring.aop.web.EnableAutoAopWebHandler;
import com.benefitj.spring.aop.log.EnableRequestLoggingHandler;
import com.benefitj.spring.eventbus.EnableEventBusPoster;
import com.benefitj.spring.security.url.AnnotationUrlRegistryConfigurerCustomizer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.web.filter.OncePerRequestFilter;
import tk.mybatis.spring.annotation.MapperScan;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@EntityScan("com.benefitj.system.model")
@MapperScan("com.benefitj.system.mapper")
@EnableAutoAopWebHandler // AOP
@EnableRequestLoggingHandler // 打印请求日志
@EnableQuartzConfuration // quartz
@EnableDruidConfuration // druid
@EnableEventBusPoster // EventBus
@EnableScaffoldWebSecurityConfiguration // web security
@Configuration
public class CommonsConfiguration {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Bean
  public OncePerRequestFilter anyRequestDoSomethingFilter(EventBusPoster poster) {
    return new OncePerRequestFilter() {
      @Override
      protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
          poster.postSync(request);
        } finally {
          filterChain.doFilter(request, response);
        }
      }
    };
  }

  @Bean
  public AnnotationUrlRegistryConfigurerCustomizer annotationUrlAuthorizationConfigurerCustomizer() {
    return new AnnotationUrlRegistryConfigurerCustomizer() {
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
