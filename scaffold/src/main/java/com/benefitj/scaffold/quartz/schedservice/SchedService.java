package com.benefitj.scaffold.quartz.schedservice;


import java.lang.annotation.*;

/**
 * quartz调度的服务
 *
 * @author DINGXIUAN
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SchedService {

  /**
   * 名称，如果为空，根据类和方法明产生
   */
  String name() default "";

  /**
   * 调度参数，默认为空
   */
  SchedParam[] params() default {};

  /**
   * 对此功能的描述
   */
  String description() default "";

}
