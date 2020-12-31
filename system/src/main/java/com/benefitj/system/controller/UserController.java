package com.benefitj.system.controller;


import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.page.PageBody;
import com.benefitj.scaffold.page.PageableRequest;
import com.benefitj.scaffold.security.token.JwtTokenManager;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.AopWebPointCut;
import com.benefitj.system.model.SysUser;
import com.benefitj.system.service.SysUserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户
 */
@AopWebPointCut
@Api(tags = {"用户"}, description = "对用户的各种操作")
@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private SysUserService userService;

  @ApiOperation("获取用户")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String"),
  })
  @GetMapping
  public HttpResult<?> get(String id) {
    SysUser user = userService.get(id);
    return HttpResult.create(CommonStatus.OK, user);
  }

  @ApiOperation("添加用户")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "user", value = "用户数据"),
  })
  @PostMapping
  public HttpResult<?> create(SysUser user) {
    user = userService.create(user);
    return HttpResult.create(CommonStatus.CREATED, user);
  }

  @ApiOperation("更新用户")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "user", value = "用户数据"),
  })
  @PutMapping
  public HttpResult<?> update(@RequestBody SysUser user) {
    if (StringUtils.isAnyBlank(user.getId(), user.getUsername())) {
      return HttpResult.failure("用户ID和用户名都不能为空");
    }
    userService.update(user);
    return HttpResult.create(CommonStatus.CREATED, user);
  }

  @ApiOperation("删除用户")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "用户ID", dataType = "String"),
      @ApiImplicitParam(name = "force", value = "是否强制", dataType = "Boolean"),
  })
  @DeleteMapping
  public HttpResult<?> delete(String id, Boolean force) {
    int count = userService.delete(id, Boolean.TRUE.equals(force));
    return HttpResult.create(CommonStatus.NO_CONTENT, count);
  }

  @ApiOperation("改变用户的状态")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "用户ID", dataType = "String", paramType = "form"),
      @ApiImplicitParam(name = "active", value = "状态", dataType = "Boolean", paramType = "form"),
  })
  @PatchMapping("/active")
  public HttpResult<?> changeActive(String id, Boolean active) {
    if (StringUtils.isBlank(id)) {
      return HttpResult.failure("用户ID不能为空");
    }
    Boolean result = userService.changeActive(id, active);
    return HttpResult.create(CommonStatus.OK, result);
  }

  @ApiOperation("获取用户列表分页")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "page", value = "分页参数", dataType = "RequestPage"),
  })
  @GetMapping("/page")
  public HttpResult<?> getPage(@PageBody PageableRequest<SysUser> page) {
    PageInfo<SysUser> userList = userService.getPage(page);
    return HttpResult.success(userList);
  }

  @ApiOperation("获取机构的用户列表")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "orgId", value = "机构ID", dataType = "String"),
      @ApiImplicitParam(name = "active", value = "是否可用", dataType = "Boolean"),
      @ApiImplicitParam(name = "multiLevel", value = "是否返回多级机构的数据", dataType = "Boolean"),
  })
  @GetMapping("/list")
  public HttpResult<?> getPlainUserList(String orgId, Boolean active, Boolean multiLevel) {
    orgId = Checker.checkNotBlank(orgId, JwtTokenManager.currentUserId());
    if (StringUtils.isBlank(orgId)) {
      return HttpResult.failure("orgId为空");
    }
    List<SysUser> userList = userService.getUserList(orgId, active, multiLevel);
    return HttpResult.success(userList);
  }

}
