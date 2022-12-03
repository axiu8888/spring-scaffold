package com.benefitj.system.controller;

import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.security.token.JwtTokenManager;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.web.AopWebPointCut;
import com.benefitj.spring.mvc.query.PageBody;
import com.benefitj.spring.mvc.query.PageRequest;
import com.benefitj.system.model.SysRole;
import com.benefitj.system.service.SysRoleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色
 */
@Api(tags = {"角色"}, description = "对角色的各种操作")
@AopWebPointCut
@RestController
@RequestMapping("/roles")
public class RoleController {

  @Autowired
  private SysRoleService roleService;

  @ApiOperation("获取角色")
  @GetMapping
  public HttpResult<SysRole> get(@ApiParam("角色ID") String id) {
    return HttpResult.create(CommonStatus.OK, roleService.get(id));
  }

  @ApiOperation("添加角色")
  @PostMapping
  public HttpResult<SysRole> create(@ApiParam("角色数据") SysRole role) {
    return HttpResult.create(CommonStatus.CREATED, roleService.create(role));
  }

  @ApiOperation("更新角色")
  @PutMapping
  public HttpResult<SysRole> update(@ApiParam("角色") @RequestBody SysRole role) {
    if (StringUtils.isAnyBlank(role.getId(), role.getName())) {
      return HttpResult.failure("角色ID和角色名都不能为空");
    }
    return HttpResult.create(CommonStatus.CREATED, roleService.update(role));
  }

  @ApiOperation("删除角色")
  @DeleteMapping
  public HttpResult<Integer> delete(@ApiParam("角色ID") String id, @ApiParam("是否强制") Boolean force) {
    return HttpResult.create(CommonStatus.NO_CONTENT, roleService.delete(id, Boolean.TRUE.equals(force)));
  }

  @ApiOperation("改变角色的状态")
  @PatchMapping("/active")
  public HttpResult<Boolean> changeActive(@ApiParam("角色ID") String id,
                                          @ApiParam("状态") Boolean active) {
    if (StringUtils.isBlank(id)) {
      return HttpResult.failure("角色ID不能为空");
    }
    return HttpResult.create(CommonStatus.OK, roleService.changeActive(id, active));
  }

  @ApiOperation("获取角色列表分页")
  @GetMapping("/page")
  public HttpResult<PageInfo<SysRole>> getPage(@ApiParam("分页参数") @PageBody PageRequest<SysRole> page) {
    return HttpResult.success(roleService.getPage(page));
  }

  @ApiOperation("获取机构的角色列表")
  @GetMapping("/list")
  public HttpResult<List<SysRole>> getList(@ApiParam("角色条件") @RequestBody SysRole condition,
                                           @ApiParam("是否返回多级机构的数据") Boolean multiLevel) {
    condition.setOrgId(Checker.checkNotBlank(condition.getOrgId(), JwtTokenManager.currentOrgId()));
    if (StringUtils.isBlank(condition.getOrgId())) {
      return HttpResult.failure("orgId为空");
    }
    List<SysRole> roleList = roleService.getList(condition, null, null, multiLevel);
    return HttpResult.success(roleList);
  }

}
