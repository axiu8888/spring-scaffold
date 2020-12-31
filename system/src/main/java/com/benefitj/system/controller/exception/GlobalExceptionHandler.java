package com.benefitj.system.controller.exception;

import com.benefitj.scaffold.LogicException;
import com.benefitj.scaffold.security.exception.PermissionException;
import com.benefitj.scaffold.vo.HttpResult;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * 请求方法不支持
   */
  @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
  public HttpResult<?> httpRequestMethodNotSupported(HttpServletRequest req,
                                                     HttpRequestMethodNotSupportedException e) {
    LOG.error("[{}]请求出错: {}", req.getRequestURI(), e.getMessage());
    return HttpResult.failure(400, e.getMessage());
  }

  /**
   * JWT过期的异常
   */
  @ExceptionHandler(value = ExpiredJwtException.class)
  public HttpResult expiredJwtExceptionHandler(HttpServletRequest req, ExpiredJwtException e) {
    LOG.error("[{}]请求出错: {}", req.getRequestURI(), e.getMessage(), e);
    return HttpResult.failure(403, "Authorization failure!");
  }

  /**
   * 客户端被中断的异常，比如原本弹出的是浏览器的下载，然后被改为了迅雷下载
   */
  @ExceptionHandler(value = ClientAbortException.class)
  public HttpResult clientAbortExceptionHandler(HttpServletRequest req, ClientAbortException e) {
    LOG.error("[" + req.getRequestURI() + "]请求出错: ", e);
    if (StringUtils.isBlank(e.getMessage())) {
      return HttpResult.failure(500, "服务器错误");
    }
    return HttpResult.failure(500, e.getMessage());
  }

  @ExceptionHandler(value = {IllegalStateException.class, IllegalArgumentException.class, LogicException.class, PermissionException.class})
  public HttpResult simpleExceptionHandler(HttpServletRequest req, Throwable e) {
    LOG.error("[{}]请求出错: {}, e.getClass(): {}", req.getRequestURI(), e.getMessage(), e.getClass());
    if (StringUtils.isBlank(e.getMessage())) {
      e.printStackTrace();
      return HttpResult.failure(500, "服务器错误");
    }
    return HttpResult.failure(400, e.getMessage());
  }

  @ExceptionHandler(value = Throwable.class)
  public HttpResult defaultHandler(HttpServletRequest req, Throwable e) {
    LOG.error("[" + req.getRequestURI() + "]请求出错: "+ e.getMessage(), e);
    if (StringUtils.isBlank(e.getMessage())) {
      e.printStackTrace();
      return HttpResult.failure(500, "服务器错误");
    }
    return HttpResult.failure(400, e.getMessage());
  }

}
