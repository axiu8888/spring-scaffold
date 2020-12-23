package com.benefitj.scaffold.urlregistry;

import org.springframework.http.HttpMethod;

import java.lang.annotation.*;

/**
 * 请求路径的过滤器
 */
@Inherited
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface UrlRegistryPermitted {

  /**
   * 支持的方法，默认全部
   */
  HttpMethod[] methods() default {};

  /**
   * 匹配的规则: /api/test/**
   */
  String[] antPatterns();

}
