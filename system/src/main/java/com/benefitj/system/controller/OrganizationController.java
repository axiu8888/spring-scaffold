package com.benefitj.system.controller;


import com.benefitj.scaffold.common.Checker;
import com.benefitj.scaffold.common.page.RequestPage;
import com.benefitj.scaffold.security.token.JwtTokenManager;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.AopWebPointCut;
import com.benefitj.system.model.SysOrganization;
import com.benefitj.system.service.SysOrganizationService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 机构
 */
@AopWebPointCut
@Api(tags = {"机构"}, description = "对机构的各种操作")
@RestController
@RequestMapping("/organizations")
public class OrganizationController {

  @Autowired
  private SysOrganizationService orgService;

  @ApiOperation("获取机构")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "机构ID", required = true, dataType = "String"),
  })
  @GetMapping
  public HttpResult<?> get(String id) {
    SysOrganization org = orgService.get(id);
    return HttpResult.create(CommonStatus.OK, org);
  }

  @ApiOperation("添加机构")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "org", value = "机构数据", required = true, dataType = "SysOrganization"),
  })
  @PostMapping
  public HttpResult<?> create(SysOrganization org) {
    org = orgService.create(org);
    return HttpResult.create(CommonStatus.CREATED, org);
  }

  @ApiOperation("更新机构")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "org", value = "机构数据", required = true, dataType = "SysOrganization"),
  })
  @PutMapping
  public HttpResult<?> update(@RequestBody SysOrganization org) {
    if (StringUtils.isAnyBlank(org.getId(), org.getName())) {
      return HttpResult.failure("机构ID和机构名不能为空");
    }
    org = orgService.update(org);
    return HttpResult.create(CommonStatus.CREATED, org);
  }

  @ApiOperation("删除机构")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "机构ID", dataType = "String"),
      @ApiImplicitParam(name = "force", value = "是否强制", dataType = "Boolean"),
  })
  @DeleteMapping
  public HttpResult<?> delete(String id, Boolean force) {
    int count = orgService.delete(id, Boolean.TRUE.equals(force));
    return HttpResult.create(CommonStatus.NO_CONTENT, count);
  }

  @ApiOperation("改变机构的状态")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "机构ID", dataType = "String", paramType = "form"),
      @ApiImplicitParam(name = "status", value = "状态", dataType = "Boolean", paramType = "form"),
  })
  @PatchMapping("/active")
  public HttpResult<?> changeActive(String id, Boolean status) {
    if (StringUtils.isBlank(id)) {
      return HttpResult.success();
    }
    Boolean result = orgService.changeActive(id, status);
    return HttpResult.create(CommonStatus.OK, result);
  }

  @ApiOperation("获取子机构列表分页")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "page", value = "分页参数", dataType = "RequestPage"),
  })
  @GetMapping("/page")
  public HttpResult<?> getPage(RequestPage<SysOrganization> page) {
    PageInfo<SysOrganization> orgList = orgService.getPage(page);
    return HttpResult.success(orgList);
  }

  @ApiOperation("获取全部的机构")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "机构ID", dataType = "String"),
      @ApiImplicitParam(name = "active", value = "是否可用", dataType = "Boolean"),
  })
  @GetMapping("/list")
  public HttpResult<?> getOrgList(String id, Boolean active) {
    id = Checker.checkNotBlank(id, JwtTokenManager.currentOrgId());
    if (StringUtils.isBlank(id)) {
      return HttpResult.failure("机构id不能为空");
    }
    List<SysOrganization> orgList = orgService.getMultiOrgList(id, active, true);
    return HttpResult.success(orgList);
  }

  @ApiOperation("获取子机构")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "机构ID", dataType = "String"),
      @ApiImplicitParam(name = "active", value = "是否可用", dataType = "Boolean"),
  })
  @GetMapping("/children")
  public HttpResult<?> getChildren(String id, Boolean active) {
    id = Checker.checkNotBlank(id, JwtTokenManager.currentOrgId());
    if (StringUtils.isBlank(id)) {
      return HttpResult.success(Collections.emptyList());
    }
    List<SysOrganization> orgList = orgService.getPlainChildren(id, active);
    return HttpResult.success(orgList);
  }

  @ApiOperation("获取组织机构树")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "机构ID", dataType = "String"),
      @ApiImplicitParam(name = "active", value = "是否可用", dataType = "Boolean"),
  })
  @GetMapping("/tree")
  public HttpResult<?> getOrgTree(String id, Boolean active) {
    if (StringUtils.isBlank(id)) {
      return HttpResult.failure("机构id不能为空");
    }
    List<SysOrganization> list = orgService.getOrgTreeList(id, active);
    return HttpResult.success(list);
  }

}
