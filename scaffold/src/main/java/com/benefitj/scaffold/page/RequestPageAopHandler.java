package com.benefitj.scaffold.page;

import com.alibaba.fastjson.JSONObject;
import com.benefitj.core.ReflectUtils;
import com.benefitj.spring.aop.WebPointCutHandler;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分页请求的处理
 */
public class RequestPageAopHandler implements WebPointCutHandler {

  private final Map<Method, RequestPageMethod> rpmMap = new ConcurrentHashMap<>();

  @Override
  public void doBefore(JoinPoint joinPoint) {
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    RequestPageMethod rpm = rpmMap.get(method);
    if (rpm == null) {
      rpm = new RequestPageMethod(method);
      final Parameter[] parameters = method.getParameters();
      rpm.setParameters(Collections.unmodifiableList(Arrays.asList(parameters)));
      for (int i = 0; i < parameters.length; i++) {
        Parameter parameter = parameters[i];
        if (parameter.getType().isAssignableFrom(PageableRequest.class)) {
          rpm.setPageParameter(parameter);
          Type type = parameter.getParameterizedType();
          if (type instanceof ParameterizedType) {
            Class<?> genericType = ReflectUtils.getActualType(type, 0);
            rpm.setGenericType(genericType);
            rpm.setParameterIndex(i);
          }
          break;
        }
      }
      rpmMap.put(method, rpm);
    }

    if (rpm.match()) {
      final Object[] args = joinPoint.getArgs();
      if (args != null && args.length > 0) {
        HttpServletRequest request = getRequest();
        JSONObject json = new JSONObject();
        request.getParameterMap().forEach((name, values) ->
            json.put(name, values.length == 1 ? values[0] : values));
        PageableRequest<Object> page = (PageableRequest<Object>) args[rpm.getParameterIndex()];
        Object condition = json.toJavaObject(rpm.getGenericType());
        page.setCondition(condition);
      }
    }
  }


  public static class RequestPageMethod {
    /**
     * Method
     */
    private final Method method;
    /**
     * 参数
     */
    private List<Parameter> parameters;
    /**
     * 分页参数
     */
    private Parameter pageParameter;
    /**
     * 泛型类型
     */
    private Class<?> genericType;
    /**
     * 参数的下标位置
     */
    private int parameterIndex;

    public RequestPageMethod(Method method) {
      this.method = method;
    }

    public Method getMethod() {
      return method;
    }

    public Parameter getPageParameter() {
      return pageParameter;
    }

    public void setPageParameter(Parameter pageParameter) {
      this.pageParameter = pageParameter;
    }

    public List<Parameter> getParameters() {
      return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
      this.parameters = parameters != null ? parameters : Collections.emptyList();
    }

    public Class<?> getGenericType() {
      return genericType;
    }

    public void setGenericType(Class<?> genericType) {
      this.genericType = genericType;
    }

    public int getParameterIndex() {
      return parameterIndex;
    }

    public void setParameterIndex(int parameterIndex) {
      this.parameterIndex = parameterIndex;
    }

    /**
     * 是否匹配
     */
    public boolean match() {
      final Class<?> pt = getGenericType();
      return pt != null && pt != Object.class;
    }

  }

}
