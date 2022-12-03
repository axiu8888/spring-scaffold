package com.benefitj.system.controller;

import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.web.AopWebPointCut;
import com.benefitj.system.model.SysRole;
import com.benefitj.system.service.UserAuthorizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 授权
 */
@Api(tags = {"授权"}, description = "对用户和角色授予或解除权限")
@AopWebPointCut
@RestController
@RequestMapping("/authorizations")
public class AuthorizationController {

  @Autowired
  private UserAuthorizationService userAuthorizationService;

  @ApiOperation("获取用户拥有的角色")
  @GetMapping("/role")
  public HttpResult<List<SysRole>> getUserOwnedRoleList(@ApiParam("用户ID") String userId) {
    if (StringUtils.isAnyBlank(userId)) {
      return HttpResult.failure("用户ID不能为空");
    }
    return HttpResult.success(userAuthorizationService.getUserOwnedRoleList(userId));
  }

  @ApiOperation("给用户添加角色")
  @PostMapping("/role")
  public HttpResult<Integer> correlation(@ApiParam("用户ID") String userId,
                                         @ApiParam("角色ID") String[] roles) {
    if (StringUtils.isBlank(userId) && StringUtils.isAnyBlank(roles)) {
      return HttpResult.failure("用户和角色都不能为空");
    }
    return HttpResult.success(userAuthorizationService.correlate(userId, roles));
  }

  @ApiOperation("删除用户的角色")
  @DeleteMapping("/role")
  public HttpResult<Integer> uncorrelation(@ApiParam("用户ID") String userId,
                                           @ApiParam("角色ID") String[] roles) {
    if (StringUtils.isAnyBlank(userId) && StringUtils.isAnyBlank(roles)) {
      return HttpResult.failure("用户和角色都不能为空");
    }
    return HttpResult.success(userAuthorizationService.uncorrelate(userId, roles));
  }


}
