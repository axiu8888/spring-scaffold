package com.benefitj.scaffold.security;

import com.benefitj.scaffold.common.Checker;
import com.benefitj.scaffold.vo.HttpResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 认证失败
 */
@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private static final Logger log = LoggerFactory.getLogger(JwtTokenAuthenticationProcessingFilter.class);

  private ObjectMapper mapper = new ObjectMapper();

  public JwtAuthenticationFailureHandler() {
  }

  public JwtAuthenticationFailureHandler(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request,
                                      HttpServletResponse response,
                                      AuthenticationException e) throws IOException, ServletException {
    String error = e.getMessage();
    log.info("认证失败: {}", error);
    HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;
    response.setStatus(unauthorized.value());
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    HttpResult<Object> failure = HttpResult.failure(unauthorized.value(), Checker.checkNotBlank(error, "Unauthorized"));
    getMapper().writeValue(response.getWriter(), failure);
  }

  public ObjectMapper getMapper() {
    return mapper;
  }

  public void setMapper(ObjectMapper mapper) {
    this.mapper = mapper;
  }
}
