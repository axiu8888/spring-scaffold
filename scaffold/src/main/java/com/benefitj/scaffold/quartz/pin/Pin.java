package com.benefitj.scaffold.quartz.pin;


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
public @interface Pin {

  /**
   * 名称，如果为空，根据类和方法明产生
   */
  String name() default "";

  /**
   * 对此功能的描述
   */
  String description() default "";

  /**
   * 定义接收的参数，最终放在一个JSON字符串中
   */
  PinParam[] params() default {};

}
