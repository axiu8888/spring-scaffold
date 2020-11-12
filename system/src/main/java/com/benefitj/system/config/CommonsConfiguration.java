package com.benefitj.system.config;

import com.benefitj.quartz.spring.EnableQuartzConfuration;
import com.benefitj.scaffold.spring.EnableDruidConfuration;
import com.benefitj.scaffold.spring.EnableScaffoldWebSecurityConfiguration;
import com.benefitj.spring.aop.EnableAutoAopWebHandler;
import com.benefitj.spring.aop.log.EnableRequestLoggingHandler;
import com.benefitj.spring.applicationevent.EnableAutoApplicationListener;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

@EntityScan("com.benefitj.system.model")
@MapperScan("com.benefitj.system.mapper")
@EnableAutoAopWebHandler // AOP
@EnableRequestLoggingHandler // 打印请求日志
@EnableAutoApplicationListener // Application Event
@EnableQuartzConfuration // quartz
@EnableDruidConfuration // druid
@EnableScaffoldWebSecurityConfiguration // web security
@Configuration
public class CommonsConfiguration {
}
