package com.benefitj.system.controller;


import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.security.token.JwtTokenManager;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.web.AopWebPointCut;
import com.benefitj.spring.mvc.page.PageableRequest;
import com.benefitj.spring.mvc.request.GetBody;
import com.benefitj.spring.mvc.request.PageBody;
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

  @ApiOperation("获取用户信息")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String", dataTypeClass = String.class),
  })
  @GetMapping
  public HttpResult<?> get(String id) {
    id = Checker.checkNotBlank(id, JwtTokenManager.currentUserId());
    SysUser userInfo = userService.get(id);
    return HttpResult.create(CommonStatus.OK, userInfo);
  }

  @ApiOperation("更新用户信息")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "user", value = "用户信息", dataTypeClass = SysUser.class),
  })
  @PutMapping
  public HttpResult<?> update(@RequestBody SysUser user) {
    if (StringUtils.isAnyBlank(user.getId())) {
      return HttpResult.failure("用户ID不能为空");
    }
    user = userService.save(user);
    return HttpResult.create(CommonStatus.CREATED, user);
  }

  @ApiOperation("获取用户列表")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "orgId", value = "机构ID", dataType = "String", dataTypeClass = String.class),
      @ApiImplicitParam(name = "active", value = "是否可用", dataType = "Boolean", dataTypeClass = Boolean.class),
      @ApiImplicitParam(name = "gender", value = "性别", dataType = "Boolean", dataTypeClass = Boolean.class),
      @ApiImplicitParam(name = "multiLevel", value = "是否返回多级机构的数据", dataType = "Boolean", dataTypeClass = Boolean.class),
  })
  @GetMapping("/list")
  public HttpResult<?> getList(@GetBody SysUser condition, Boolean multiLevel) {
    condition.setOrgId(Checker.checkNotBlank(condition.getOrgId(), JwtTokenManager.currentOrgId()));
    if (StringUtils.isBlank(condition.getOrgId())) {
      return HttpResult.failure("orgId为空");
    }
    List<SysUser> users = userService.getList(condition, null, null, multiLevel);
    return HttpResult.success(users);
  }

  @ApiOperation("获取用户列表分页")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "page", value = "分页参数", dataType = "RequestPage", dataTypeClass = PageableRequest.class),
  })
  @GetMapping("/page")
  public HttpResult<?> getPage(@PageBody PageableRequest<SysUser> page) {
    PageInfo<SysUser> pageInfo = userService.getPage(page);
    return HttpResult.success(pageInfo);
  }

}
