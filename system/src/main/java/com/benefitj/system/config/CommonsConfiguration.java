package com.benefitj.system.config;

import com.benefitj.quartz.config.EnableQuartzConfuration;
import com.benefitj.scaffold.config.EnableDruidConfuration;
import com.benefitj.scaffold.config.EnableScaffoldWebSecurityConfiguration;
import com.benefitj.spring.aop.EnableAutoAopWebHandler;
import com.benefitj.spring.aop.log.EnableRequestLoggingHandler;
import com.benefitj.spring.applicationevent.EnableAutoApplicationListener;
import com.benefitj.spring.ctx.EnableSpringCtxInit;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

@EntityScan("com.benefitj.system.model")
@MapperScan("com.benefitj.system.mapper")
@EnableSpringCtxInit // Spring Context
@EnableAutoAopWebHandler // AOP
@EnableRequestLoggingHandler // 打印请求日志
@EnableAutoApplicationListener // Application Event
// 需要的依赖
@EnableQuartzConfuration // quartz
@EnableDruidConfuration // druid
@EnableScaffoldWebSecurityConfiguration // web security
@Configuration
public class CommonsConfiguration {
}
