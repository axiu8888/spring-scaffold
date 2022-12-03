package com.benefitj.system.controller;


import com.benefitj.scaffold.security.token.JwtTokenManager;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.web.AopWebPointCut;
import com.benefitj.system.model.SysAccount;
import com.benefitj.system.service.SysAccountService;
import com.benefitj.system.service.UserAuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 账号
 */
@Api(tags = {"账号"}, description = "对账号的各种操作")
@AopWebPointCut
@RestController
@RequestMapping("/account")
public class AccountController {

  @Autowired
  private SysAccountService accountService;
  @Autowired
  private UserAuthenticationService userAuthenticationService;

  @ApiOperation("获取账号")
  @GetMapping
  public HttpResult<SysAccount> get(@ApiParam("ID") String id) {
    if (StringUtils.isBlank(id)) {
      id = JwtTokenManager.currentUserId();
    }
    SysAccount user = accountService.get(id);
    return HttpResult.create(CommonStatus.OK, user);
  }

  @ApiOperation("添加账号")
  @PostMapping
  public HttpResult<SysAccount> create(@ApiParam("账号") SysAccount account) {
    account = accountService.save(account);
    return HttpResult.create(CommonStatus.CREATED, account);
  }

  @ApiOperation("删除账号")
  @DeleteMapping
  public HttpResult<Integer> delete(@ApiParam("ID") String id, @ApiParam("是否强制") Boolean force) {
    int count = userAuthenticationService.deleteAccount(id, force);
    return HttpResult.create(CommonStatus.NO_CONTENT, count);
  }

  @ApiOperation("改变账号的状态")
  @PatchMapping("/active")
  public HttpResult<Boolean> changeActive(@ApiParam("ID") String id, @ApiParam("状态") Boolean active) {
    if (StringUtils.isBlank(id)) {
      return HttpResult.failure("账号ID不能为空");
    }
    Boolean result = accountService.changeActive(id, active);
    return HttpResult.create(CommonStatus.OK, result);
  }

  //@AopIgnore
  @ApiOperation("修改密码")
  @PostMapping("/changePassword")
  public HttpResult<Boolean> changePassword(@ApiParam("用户ID") String userId,
                                      @ApiParam("旧密码") String oldPassword,
                                      @ApiParam("新密码") String newPassword) {
    if (StringUtils.isAnyBlank(userId, oldPassword, newPassword)) {
      return HttpResult.failure("用户ID和密码都不能为空");
    }
    boolean result = accountService.changePassword(userId, oldPassword, newPassword);
    return HttpResult.create(CommonStatus.OK, result);
  }

}
