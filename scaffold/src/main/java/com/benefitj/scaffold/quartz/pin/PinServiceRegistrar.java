package com.benefitj.scaffold.quartz.pin;

import com.benefitj.spring.registrar.AnnotationMetadata;
import com.benefitj.spring.registrar.AnnotationMetadataRegistrar;
import com.benefitj.spring.registrar.MethodElement;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 调度服务注册器
 *
 * @author DINGXIUAN
 */
public class PinServiceRegistrar implements AnnotationMetadataRegistrar {

  private PinManager pinManager;

  public PinServiceRegistrar() {
  }

  public PinManager getWorkerPinManager() {
    return pinManager;
  }

  public void setWorkerPinManager(PinManager pinManager) {
    this.pinManager = pinManager;
  }

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
      Pin pin = (Pin) element.getAnnotations()[0];
      // 处理被注解的对象
      Parameter[] parameters = method.getParameters();
      for (Parameter parameter : parameters) {
        Class<?> parameterType = parameter.getType();
        if (!supportType(parameterType)) {
          throw new IllegalArgumentException(String.format(
              "[%s.%s]的参数仅支持String类型: %s[%s]"
              , method.getDeclaringClass().getName()
              , method.getName()
              , parameter.getName()
              , parameterType.getName()
          ));
        }
      }
      String name = pin.name();

      PinManager manager = getWorkerPinManager();
      if (manager.containsKey(name)) {
        Method amMethod = manager.get(name).getMethod();
        throw new IllegalStateException(String.format("%s.%s，已存在一个相同名称的服务: %s.%s"
            , method.getDeclaringClass().getName(), method.getName()
            , amMethod.getDeclaringClass().getName(), amMethod.getName()
        ));
      }
      manager.put(name, new PinMethod(method, pin));
    }
  }

  public boolean supportType(Class<?> parameterType) {
    return parameterType == String.class;
  }

}
