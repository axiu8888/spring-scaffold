package com.benefitj.scaffold.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 引入 druid 配置
 */
@Import({DruidConfiguration.class})
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableDruidConfuration {
}
