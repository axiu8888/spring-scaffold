package com.benefitj.system.controller;

import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.AopWebPointCut;
import com.benefitj.system.model.SysRole;
import com.benefitj.system.service.UserAuthorizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 授权
 */
@AopWebPointCut
@Api(tags = {"授权"}, description = "对用户和角色授予或解除权限")
@RestController
@RequestMapping("/authorizations")
public class AuthorizationController {

  @Autowired
  private UserAuthorizationService userAuthorizationService;

  @ApiOperation("获取用户拥有的角色")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String"),
  })
  @GetMapping("/role")
  public HttpResult<?> getUserOwnedRoleList(String userId) {
    if (StringUtils.isAnyBlank(userId)) {
      return HttpResult.failure("用户ID不能为空");
    }
    List<SysRole> roleList = userAuthorizationService.getUserOwnedRoleList(userId);
    return HttpResult.success(roleList);
  }

  @ApiOperation("给用户添加角色")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String"),
      @ApiImplicitParam(name = "roles", value = "角色ID", required = true, dataType = "String[]"),
  })
  @PostMapping("/role")
  public HttpResult<?> correlation(String userId, String[] roles) {
    if (StringUtils.isBlank(userId) && StringUtils.isAnyBlank(roles)) {
      return HttpResult.failure("用户和角色都不能为空");
    }
    int count = userAuthorizationService.correlate(userId, roles);
    return HttpResult.success(count);
  }

  @ApiOperation("删除用户的角色")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String"),
      @ApiImplicitParam(name = "roles", value = "角色ID", required = true, dataType = "String[]"),
  })
  @DeleteMapping("/role")
  public HttpResult<?> uncorrelation(String userId, String[] roles) {
    if (StringUtils.isAnyBlank(userId) && StringUtils.isAnyBlank(roles)) {
      return HttpResult.failure("用户和角色都不能为空");
    }
    int count = userAuthorizationService.uncorrelate(userId, roles);
    return HttpResult.success(count);
  }


}
