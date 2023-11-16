package com.benefitj.scaffold.quartz;


import com.benefitj.scaffold.quartz.entity.SysJob;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.Method;

/**
 * bean方法调用
 */
@SuperBuilder
@NoArgsConstructor
@Data
public class SpringBeanMethodJobWorker {

  /**
   * 对象
   */
  private Object target;
  /**
   * 方法
   */
  private Method method;
  /**
   * 调度服务
   */
  private SysJob jobTask;

}
