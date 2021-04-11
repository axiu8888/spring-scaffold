package com.benefitj.scaffold.mapper;

import com.benefitj.core.ReflectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface EntityFinder {

  /**
   * 获取实体类
   *
   * @param mapper        Mapper接口
   * @param typeParamName 泛型参数名称
   * @param <T>           类型
   * @return 返回获取的实体类
   */
  <T> Class<T> getEntityClass(Class<?> mapper, String typeParamName);

  /**
   * 默认实现
   */
  EntityFinder INSTANCE = new SimpleEntityFinder();

  class SimpleEntityFinder implements EntityFinder {

    /**
     * 缓存实体类
     */
    private final Map<Class<?>, Class> entityMap = new ConcurrentHashMap<>();

    public SimpleEntityFinder() {
    }

    /**
     * 获取实体类类型
     */
    @Override
    public <T> Class<T> getEntityClass(Class<?> mapper, String typeParamName) {
      Class<T> entityClass = (Class<T>) entityMap.get(mapper);
      if (entityClass != null) {
        return entityClass;
      }
      entityClass = ReflectUtils.getParameterizedTypeClass(mapper, typeParamName);
      if (entityClass == null) {
        throw new IllegalStateException("无法获取实体类型!");
      }
      entityMap.put(mapper, entityClass);
      return entityClass;
    }
  }

}
