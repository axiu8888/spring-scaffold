package com.benefitj.scaffold.quartz.schedservice;


import java.lang.annotation.*;

/**
 * 方法参数
 *
 * @author DINGXIUAN
 */
@Documented
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SchedParam {

  /**
   * 参数名称，如果未设置啊，
   */
  String name() default "";

  /**
   * A brief description of the parameter.
   */
  String value() default "";

  /**
   * Describes the default value for the parameter.
   */
  String defaultValue() default "";

}
