package com.benefitj.system.controller;


import com.benefitj.scaffold.security.token.JwtTokenManager;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.web.AopWebPointCut;
import com.benefitj.system.model.SysAccount;
import com.benefitj.system.service.SysAccountService;
import com.benefitj.system.service.UserAuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 账号
 */
@AopWebPointCut
@Api(tags = {"账号"}, description = "对账号的各种操作")
@RestController
@RequestMapping("/account")
public class AccountController {

  @Autowired
  private SysAccountService accountService;
  @Autowired
  private UserAuthenticationService userAuthenticationService;

  @ApiOperation("获取账号")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "账号ID", required = true, dataType = "String", dataTypeClass = String.class),
  })
  @GetMapping
  public HttpResult<?> get(String id) {
    if (StringUtils.isBlank(id)) {
      id = JwtTokenManager.currentUserId();
    }
    SysAccount user = accountService.get(id);
    return HttpResult.create(CommonStatus.OK, user);
  }

  @ApiOperation("添加账号")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "account", value = "账号数据", dataType = "String", dataTypeClass = String.class),
  })
  @PostMapping
  public HttpResult<?> create(SysAccount account) {
    account = accountService.save(account);
    return HttpResult.create(CommonStatus.CREATED, account);
  }

  @ApiOperation("删除账号")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "账号ID", dataType = "String", dataTypeClass = String.class),
      @ApiImplicitParam(name = "force", value = "是否强制", dataType = "Boolean", dataTypeClass = Boolean.class),
  })
  @DeleteMapping
  public HttpResult<?> delete(String id, Boolean force) {
    int count = userAuthenticationService.deleteAccount(id, force);
    return HttpResult.create(CommonStatus.NO_CONTENT, count);
  }

  @ApiOperation("改变账号的状态")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "账号ID", dataType = "String", paramType = "form", dataTypeClass = String.class),
      @ApiImplicitParam(name = "active", value = "状态", dataType = "Boolean", paramType = "form", dataTypeClass = Boolean.class),
  })
  @PatchMapping("/active")
  public HttpResult<?> changeActive(String id, Boolean active) {
    if (StringUtils.isBlank(id)) {
      return HttpResult.failure("账号ID不能为空");
    }
    Boolean result = accountService.changeActive(id, active);
    return HttpResult.create(CommonStatus.OK, result);
  }

  //@AopIgnore
  @ApiOperation("修改密码")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", paramType = "form", dataTypeClass = String.class),
      @ApiImplicitParam(name = "oldPassword", value = "旧密码", dataType = "String", paramType = "form", dataTypeClass = String.class),
      @ApiImplicitParam(name = "newPassword", value = "新密码", dataType = "String", paramType = "form", dataTypeClass = String.class),
  })
  @PostMapping("/changePassword")
  public HttpResult<?> changePassword(String userId, String oldPassword, String newPassword) {
    if (StringUtils.isAnyBlank(userId, oldPassword, newPassword)) {
      return HttpResult.failure("用户ID和密码都不能为空");
    }
    boolean result = accountService.changePassword(userId, oldPassword, newPassword);
    return HttpResult.create(CommonStatus.OK, result);
  }

}
