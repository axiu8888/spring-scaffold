package com.benefitj.scaffold.mapper;


import java.lang.annotation.*;


/**
 * LEFT Join Column
 *
 * @author DINGXIUAN
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LeftOuterJoinColumn {

  /**
   *
   */
  String table();

  /**
   *
   */
  String[] columns();



}
