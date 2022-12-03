package com.benefitj.system.controller;


import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.security.token.JwtTokenManager;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.web.AopWebPointCut;
import com.benefitj.spring.mvc.query.PageBody;
import com.benefitj.spring.mvc.query.PageRequest;
import com.benefitj.system.model.SysUser;
import com.benefitj.system.service.SysUserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户
 */
@Api(tags = {"用户"}, description = "对用户的各种操作")
@AopWebPointCut
@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private SysUserService userService;

  @ApiOperation("获取用户信息")
  @GetMapping
  public HttpResult<SysUser> get(@ApiParam("用户ID") String id) {
    id = Checker.checkNotBlank(id, JwtTokenManager.currentUserId());
    return HttpResult.create(CommonStatus.OK, userService.get(id));
  }

  @ApiOperation("更新用户信息")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "user", value = "用户信息", dataTypeClass = SysUser.class),
  })
  @PutMapping
  public HttpResult<SysUser> update(@ApiParam("用户") @RequestBody SysUser user) {
    if (StringUtils.isAnyBlank(user.getId())) {
      return HttpResult.failure("用户ID不能为空");
    }
    return HttpResult.create(CommonStatus.CREATED, userService.save(user));
  }

  @ApiOperation("获取用户列表")
  @GetMapping("/list")
  public HttpResult<List<SysUser>> getList(@ApiParam("条件") @RequestBody SysUser condition,
                                           @ApiParam("是否返回多级机构的数据") Boolean multiLevel) {
    condition.setOrgId(Checker.checkNotBlank(condition.getOrgId(), JwtTokenManager.currentOrgId()));
    if (StringUtils.isBlank(condition.getOrgId())) {
      return HttpResult.failure("orgId为空");
    }
    return HttpResult.success(userService.getList(condition, null, null, multiLevel));
  }

  @ApiOperation("获取用户列表分页")
  @GetMapping("/page")
  public HttpResult<PageInfo<SysUser>> getPage(@ApiParam("分页参数") @PageBody PageRequest<SysUser> page) {
    PageInfo<SysUser> pageInfo = userService.getPage(page);
    return HttpResult.success(pageInfo);
  }

}
