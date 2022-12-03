package com.benefitj.system.controller;

import com.benefitj.scaffold.vo.AuthTokenVo;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.AopIgnore;
import com.benefitj.spring.aop.web.AopWebPointCut;
import com.benefitj.spring.security.url.UrlPermitted;
import com.benefitj.system.model.SysAccount;
import com.benefitj.system.service.UserAuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录
 */
@Api(tags = {"用户认证"}, description = "注册/登录/刷新token")
@UrlPermitted
@AopWebPointCut
@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private UserAuthenticationService service;

  /**
   * 注册
   */
  @AopIgnore
  @ApiOperation("注册")
  @PostMapping("/register")
  public HttpResult<AuthTokenVo<SysAccount>> register(@ApiParam("用户名") String username,
                                                      @ApiParam("密码") String password,
                                                      @ApiParam("机构ID") String orgId) {
    if (StringUtils.isAnyBlank(username, password)) {
      return HttpResult.failure("用户名或密码不能为空");
    }
    return HttpResult.success(service.register(username, password, orgId));
  }

  /**
   * 登录
   */
  @AopIgnore
  @ApiOperation("登录")
  @PostMapping("/login")
  public HttpResult<AuthTokenVo<SysAccount>> login(@ApiParam("用户名") String username,
                                                   @ApiParam("密码") String password) {
    if (StringUtils.isAnyBlank(username, password)) {
      return HttpResult.failure("用户名或密码错误");
    }
    return HttpResult.success(service.login(username, password));
  }

  /**
   * 获取 token
   */
  @ApiOperation("刷新token")
  @GetMapping("/token")
  public HttpResult<AuthTokenVo<SysAccount>> refreshToken(@ApiParam("refreshToken") @RequestHeader("refresh") String refreshToken) {
    if (StringUtils.isBlank(refreshToken)) {
      return HttpResult.failure("token错误");
    }
    return HttpResult.success(service.getAccessToken(refreshToken));
  }


}
