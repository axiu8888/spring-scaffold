package com.benefitj.scaffold.page;

import java.lang.annotation.*;

/**
 * 分页请求
 *
 * @author DINGXIUAN
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
//@Inherited
public @interface PageBody {

  /**
   * 是否需要请求体
   */
  boolean required() default true;

}
