package com.benefitj.scaffold.quartz.sched;


import java.lang.annotation.*;

/**
 * 方法参数
 *
 * @author DINGXIUAN
 */
@Documented
@Target({ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AnchorParam {

  /**
   * 参数名称，如果未设置取参数的默认名，可能会取到arg0、arg1等值
   */
  String name() default "";

  /**
   * 参数的默认值
   */
  String defaultValue() default "";

  /**
   * 参数描述
   */
  String description() default "";

}
