package com.benefitj.scaffold.typehandler;

import java.io.Serializable;

/**
 * @author DINGXIUAN
 */
public interface TypeValue<T extends Serializable> {

  /**
   * 获取枚举类型的值
   */
  T getValue();
}
