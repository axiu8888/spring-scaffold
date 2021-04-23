package com.benefitj.scaffold.quartz.sched;

import com.benefitj.spring.registrar.AnnotationMetadata;
import com.benefitj.spring.registrar.AnnotationMetadataRegistrar;
import com.benefitj.spring.registrar.MethodElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

public class SchedServiceRegistrar implements AnnotationMetadataRegistrar {

  private final Logger log = LoggerFactory.getLogger(getClass());

  /**
   * 注册
   *
   * @param metadata    注解类的信息
   * @param beanFactory bean工厂
   */
  @Override
  public void register(AnnotationMetadata metadata, ConfigurableListableBeanFactory beanFactory) {
    //Object bean = metadata.getBean();
    for (MethodElement element : metadata.getMethodElements()) {
      Method method = element.getMethod();
      SchedAnchor service = (SchedAnchor) element.getAnnotations()[0];
      // 处理被注解的对象
      Class<?>[] parameterTypes = method.getParameterTypes();
      //

      log.error("{}.{}, name: {}, params: {}, remarks: {}, parameterTypes: {}"
          , method.getDeclaringClass().getName()
          , method.getName()
          , service.name()
          , service.params()
          , service.description()
          , Arrays.toString(parameterTypes)
      );
    }
  }

}
