package com.benefitj.scaffold.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 引入 Web Security 配置
 */
@Import({DefaultWebSecurityConfigurerAdapter.class})
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableScaffoldWebSecurityConfiguration {
}
