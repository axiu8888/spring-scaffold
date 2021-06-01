package com.benefitj.scaffold.quartz.pin;


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
public @interface PinParam {

  /**
   * 参数名称，如果未设置取参数的默认名，可能会取到arg0、arg1等值
   */
  String name() default "";

  /**
   * 参数描述
   */
  String description() default "";

  /**
   * 参数类型
   */
  ParamType type() default ParamType.STRING;

}
