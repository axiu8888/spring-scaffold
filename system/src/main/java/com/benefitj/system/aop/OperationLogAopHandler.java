package com.benefitj.system.aop;

import com.alibaba.fastjson.JSON;
import com.benefitj.core.EventLoop;
import com.benefitj.core.IdUtils;
import com.benefitj.core.local.LocalCache;
import com.benefitj.core.local.LocalCacheFactory;
import com.benefitj.scaffold.IpUtil;
import com.benefitj.scaffold.security.token.JwtToken;
import com.benefitj.scaffold.security.token.JwtTokenManager;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.AopAdvice;
import com.benefitj.spring.aop.web.WebPointCutHandler;
import com.benefitj.spring.mvc.matcher.AntPathRequestMatcher;
import com.benefitj.spring.mvc.matcher.OrRequestMatcher;
import com.benefitj.system.model.SysOperationLog;
import com.benefitj.system.service.SysOperationLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 日志保存
 */
@Slf4j
@Component
public class OperationLogAopHandler implements InitializingBean, WebPointCutHandler {

  @Autowired
  private SysOperationLogService operationLogService;

  /**
   * 管理切面类和方法
   */
  private final AopLogManager logManager = new AopLogManager();
  /**
   * 缓存日志信息
   */
  private final LocalCache<SysOperationLog> cache = LocalCacheFactory.newCache(SysOperationLog::new);

  private OrRequestMatcher matcher;

  @Value("#{@environment['spring.aop.operation-log.save'] ?: false}")
  private boolean save;
  /**
   * 是否支持GET请求
   */
  @Value("#{@environment['spring.aop.operation-log.ignore-get'] ?: true}")
  private boolean ignoreGet;
  /**
   * 操作日志
   */
  @Value("#{@environment['spring.aop.operation-log.ignore-urls'] ?: ''}")
  private String ignoreUrls;

  @Override
  public void afterPropertiesSet() throws Exception {
    if (StringUtils.isNotBlank(ignoreUrls)) {
      String[] split = ignoreUrls.split(",");
      matcher = new OrRequestMatcher(
          Stream.of(split)
              .map(String::trim)
              .filter(StringUtils::isNotBlank)
              .map(AntPathRequestMatcher::new)
              .collect(Collectors.toList())
      );
    }
  }

  /**
   * 方法执行之前
   *
   * @param joinPoint 方法切入点
   */
  @Override
  public void doBefore(AopAdvice advice, JoinPoint joinPoint) {
    if (!support(joinPoint)) {
      return;
    }

    MethodInfo mi = getMethodInfo(joinPoint);
    // 开始记录
    SysOperationLog sol = cache.get();
    sol.setId(String.valueOf(IdUtils.snowflakeId()));
    sol.setCreateTime(new Date());
    sol.setActive(true);

    JwtToken token = JwtTokenManager.currentToken(true);
    if (token != null) {
      sol.setOrgId(token.getOrgId());
      sol.setCreatorId(token.getUserId());
    }

    // 模块
    sol.setModule(mi.getModule());
    // 操作
    sol.setOperation(mi.getOperation());
    // 描述
    sol.setRemarks(mi.getRemarks());

    HttpServletRequest req = getRequest();

    Object[] args = joinPoint.getArgs();
    List<Object> argList = new ArrayList<>(args.length);
    for (Object arg : args) {
      if (arg instanceof ServletRequest) {
        argList.add("req");
      } else if (arg instanceof ServletResponse) {
        argList.add("response");
      } else if (arg instanceof MultipartFile) {
        MultipartFile f = (MultipartFile) arg;
        argList.add("file: " + String.format("(%s, %d)", f.getOriginalFilename(), f.getSize()));
      } else if (arg instanceof MultipartFile[]) {
        argList.add("files: " + Arrays.stream(((MultipartFile[]) arg))
            .map(f -> String.format("(%s, %d)", f.getOriginalFilename(), f.getSize()))
            .collect(Collectors.joining(", ")));
      } else {
        argList.add(arg);
      }
    }

    // 保存请求参数
    Method method = mi.getMethod();
    if (method.getParameterCount() > 0 && argList.size() == method.getParameterCount()) {
      Parameter[] parameters = method.getParameters();
      Map<String, Object> parameterMap = new LinkedHashMap<>();
      for (int i = 0; i < parameters.length; i++) {
        parameterMap.put(parameters[i].getName(), argList.get(i));
      }
      sol.setArgs(JSON.toJSONString(parameterMap));
    } else {
      sol.setArgs("");
    }

    // URI
    sol.setUrl(req.getRequestURI());
    // IP
    sol.setIpAddr(IpUtil.getIpAddress(req));
    // METHOD
    sol.setHttpMethod(req.getMethod());
    // 方法名
    sol.setClassMethod(method.getDeclaringClass().getSimpleName() + "." + method.getName());
  }

  @Override
  public void doAfter(AopAdvice advice, JoinPoint joinPoint, AtomicReference<Object> returnValue) {
    if (!support(joinPoint)) {
      return;
    }
    // 成功返回
    SysOperationLog sol = cache.get();
    if (returnValue.get() instanceof HttpResult) {
      // 返回 HttpResult
      HttpResult r = (HttpResult) returnValue.get();
      sol.setResultCode(r.getCode());
      sol.setResultMsg(r.getMsg());
    } else {
      HttpServletResponse resp = getResponse();
      sol.setResultCode(resp.getStatus());
      sol.setResultMsg("SUCCESS");
    }
  }

  @Override
  public void doAfterThrowing(AopAdvice advice, JoinPoint joinPoint, Throwable ex) {
    if (!support(joinPoint)) {
      return;
    }
    // 抛异常了
    SysOperationLog sol = cache.get();
    sol.setResultMsg(ex.getMessage());
    sol.setResultCode(500);
  }

  @Override
  public void doAfterReturning(AopAdvice advice, JoinPoint joinPoint) {
    if (!support(joinPoint)) {
      return;
    }
    // 保存
    final SysOperationLog sol = cache.getAndRemove();
    EventLoop.io().execute(() -> {
      try {
        operationLogService.insert(sol);
      } catch (Throwable ex) {
        log.error("保存操作日志失败: {}", ex.getMessage());
      }
    });
  }

  private MethodInfo getMethodInfo(JoinPoint joinPoint) {
    return logManager.getMethodInfo(joinPoint);
  }

  public boolean support(JoinPoint joinPoint) {
    if (!save) {
      return false;
    }
    HttpServletRequest request = getRequest();
    if (request != null) {
      // 不支持GET，且是GET请求
      if (ignoreGet && HttpMethod.GET.matches(request.getMethod())) {
        return false;
      }
      if (matcher != null) {
        return !matcher.matches(request);
      }
    }
    MethodInfo mi = getMethodInfo(joinPoint);
    return mi.isSupport();
  }

  /**
   * 解析并缓存切面中调用过的Class及其Method信息，方便之后直接获取
   */
  static final class AopLogManager {

    /**
     * 存储解析的ClassInfo
     */
    private final Map<Class<?>, ClassInfo> classInfoMap = new ConcurrentHashMap<>();

    /**
     * 解析Method的信息
     *
     * @param point 切入点
     * @return 返回MethodInfo
     */
    public MethodInfo getMethodInfo(JoinPoint point) {
      ClassInfo classInfo = getClassInfo(point);
      Method method = ((MethodSignature) point.getSignature()).getMethod();
      return parseMethodInfo(classInfo, method);
    }

    private MethodInfo parseMethodInfo(ClassInfo classInfo, Method method) {
      MethodInfo methodInfo = classInfo.getMethodInfo(method);
      if (methodInfo != null) {
        return methodInfo;
      }
      methodInfo = new MethodInfo(method);
      // 设置模块名
      methodInfo.setModule(classInfo.getName());
      if (method.isAnnotationPresent(ApiOperation.class)) {
        ApiOperation ap = getAnnotation(method, ApiOperation.class);
        // 操作
        methodInfo.setOperation(ap.value());
        // 描述
        methodInfo.setRemarks(ap.notes());
      }
      classInfo.addMethodInfo(method, methodInfo);
      methodInfo.setSupport(true);
      return methodInfo;
    }

    /**
     * 解析Class的信息
     */
    private ClassInfo getClassInfo(JoinPoint point) {
      Class clazz = point.getSignature().getDeclaringType();
      ClassInfo classInfo = classInfoMap.get(clazz);
      if (classInfo == null) {
        // 解析Class类
        classInfo = new ClassInfo(clazz);
        Api api = getAnnotation(clazz, Api.class);
        if (api != null) {
          classInfo.setName(api.tags()[0]);
        }
        classInfoMap.put(clazz, classInfo);
      }
      return classInfo;
    }

    private <A extends Annotation> A getAnnotation(AnnotatedElement obj, Class<A> annotationClass) {
      return obj.getAnnotation(annotationClass);
    }

  }

  /**
   * 类信息
   */
  static final class ClassInfo {
    /**
     * 声明的类
     */
    private Class clazz;
    /**
     * 方法
     */
    private final Map<Method, MethodInfo> methodInfoMap = new ConcurrentHashMap<>();
    /**
     * 模块名
     */
    private String name;

    public ClassInfo(Class clazz) {
      this.clazz = clazz;
    }

    public Class getClazz() {
      return clazz;
    }

    public void setClazz(Class clazz) {
      this.clazz = clazz;
    }

    public Map<Method, MethodInfo> getMethodInfoMap() {
      return methodInfoMap;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public int hashCode() {
      return clazz.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      return clazz.equals(obj);
    }

    public void addMethodInfo(Method method, MethodInfo info) {
      methodInfoMap.put(method, info);
    }

    public MethodInfo getMethodInfo(Method method) {
      return methodInfoMap.get(method);
    }
  }


  /**
   * 方法信息
   */
  static final class MethodInfo {

    private final Method method;
    /**
     * 模块
     */
    private String module;
    /**
     * 操作
     */
    private String operation;
    /**
     * 描述
     */
    private String remarks;
    /**
     * 是否支持
     */
    private boolean support = false;

    public MethodInfo(Method method) {
      this.method = method;
    }

    public Method getMethod() {
      return method;
    }

    public String getModule() {
      return module;
    }

    public void setModule(String module) {
      this.module = module;
    }

    public String getOperation() {
      return operation;
    }

    public void setOperation(String operation) {
      this.operation = operation;
    }

    public String getRemarks() {
      return remarks;
    }

    public void setRemarks(String remarks) {
      this.remarks = remarks;
    }

    public boolean isSupport() {
      return support;
    }

    public void setSupport(boolean support) {
      this.support = support;
    }

    public <T extends Annotation> T[] getAnnotations(Class<T> annotationClass) {
      return getMethod().getAnnotationsByType(annotationClass);
    }

    @Override
    public String toString() {
      return "MethodInfo{"
          + "method="
          + method
          + ", module='"
          + module
          + '\''
          + ", operate='"
          + operation
          + '\''
          + ", description='"
          + remarks
          + '\''
          + '}';
    }
  }

}
