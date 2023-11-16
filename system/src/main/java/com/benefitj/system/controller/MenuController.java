package com.benefitj.system.controller;

import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.web.AopWebPointCut;
import com.benefitj.spring.mvc.query.PageBody;
import com.benefitj.spring.mvc.query.PageRequest;
import com.benefitj.spring.security.jwt.token.JwtTokenManager;
import com.benefitj.system.model.SysMenu;
import com.benefitj.system.service.SysMenuService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单
 */
@Api(tags = {"菜单"}, description = "对菜单的各种操作")
@AopWebPointCut
@RestController
@RequestMapping("/menus")
public class MenuController {

  @Autowired
  private SysMenuService menuService;

  @ApiOperation("获取菜单")
  @GetMapping
  public HttpResult<SysMenu> get(@ApiParam("菜单ID") String id) {
    return HttpResult.create(CommonStatus.OK, menuService.get(id));
  }

  @ApiOperation("添加菜单")
  @PostMapping
  public HttpResult<SysMenu> create(@ApiParam("菜单") SysMenu menu) {
    return HttpResult.create(CommonStatus.CREATED, menuService.create(menu));
  }

  @ApiOperation("更新菜单")
  @PutMapping
  public HttpResult<SysMenu> update(@ApiParam("菜单") @RequestBody SysMenu menu) {
    if (StringUtils.isAnyBlank(menu.getId(), menu.getName())) {
      return HttpResult.failure("菜单ID和菜单名都不能为空");
    }
    return HttpResult.create(CommonStatus.CREATED, menuService.update(menu));
  }

  @ApiOperation("删除菜单")
  @DeleteMapping
  public HttpResult<Integer> delete(@ApiParam("菜单ID") String id, @ApiParam("是否强制") Boolean force) {
    return HttpResult.create(CommonStatus.NO_CONTENT, menuService.delete(id, Boolean.TRUE.equals(force)));
  }

  @ApiOperation("改变菜单的状态")
  @PatchMapping("/active")
  public HttpResult<Boolean> changeActive(@ApiParam("菜单ID") String id,
                                          @ApiParam("状态") Boolean active) {
    if (StringUtils.isBlank(id)) {
      return HttpResult.failure("菜单ID不能为空");
    }
    return HttpResult.create(CommonStatus.OK, menuService.changeActive(id, active));
  }

  @ApiOperation("获取菜单列表分页")
  @GetMapping("/page")
  public HttpResult<PageInfo<SysMenu>> getPage(@ApiParam("分页参数") @PageBody PageRequest<SysMenu> page) {
    return HttpResult.success(menuService.getPage(page));
  }

  @ApiOperation("获取机构的菜单列表")
  @GetMapping("/list")
  public HttpResult<List<SysMenu>> getList(@ApiParam("条件") @RequestBody SysMenu condition,
                                           @ApiParam("是否返回多级机构的数据") Boolean multiLevel) {
    condition.setOrgId(Checker.checkNotBlank(condition.getOrgId(), JwtTokenManager.currentOrgId()));
    if (StringUtils.isBlank(condition.getOrgId())) {
      return HttpResult.failure("orgId为空");
    }
    return HttpResult.success(menuService.getList(condition, null, null, multiLevel));
  }

}
