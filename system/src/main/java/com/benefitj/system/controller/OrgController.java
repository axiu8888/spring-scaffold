package com.benefitj.system.controller;


import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.security.token.JwtTokenManager;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.web.AopWebPointCut;
import com.benefitj.spring.mvc.query.PageBody;
import com.benefitj.spring.mvc.query.PageRequest;
import com.benefitj.system.model.SysOrg;
import com.benefitj.system.service.SysOrganizationService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 机构
 */
@Api(tags = {"机构"}, description = "对机构的各种操作")
@AopWebPointCut
@RestController
@RequestMapping("/org")
public class OrgController {

  @Autowired
  private SysOrganizationService orgService;

  @ApiOperation("获取机构")
  @GetMapping
  public HttpResult<SysOrg> get(@ApiParam("机构ID") String id) {
    return HttpResult.create(CommonStatus.OK, orgService.get(id));
  }

  @ApiOperation("添加机构")
  @PostMapping
  public HttpResult<SysOrg> create(@ApiParam("机构数据") SysOrg org) {
    return HttpResult.create(CommonStatus.CREATED, orgService.create(org));
  }

  @ApiOperation("更新机构")
  @PutMapping
  public HttpResult<SysOrg> update(@ApiParam("机构数据") @RequestBody SysOrg org) {
    if (StringUtils.isAnyBlank(org.getId(), org.getName())) {
      return HttpResult.failure("机构ID和机构名不能为空");
    }
    return HttpResult.create(CommonStatus.CREATED, orgService.update(org));
  }

  @ApiOperation("删除机构")
  @DeleteMapping
  public HttpResult<Integer> delete(@ApiParam("机构ID") String id,
                                    @ApiParam("是否强制") Boolean force) {
    return HttpResult.create(CommonStatus.NO_CONTENT, orgService.delete(id, Boolean.TRUE.equals(force)));
  }

  @ApiOperation("改变机构的状态")
  @PatchMapping("/active")
  public HttpResult<Boolean> changeActive(@ApiParam("机构ID") String id,
                                          @ApiParam("状态") Boolean active) {
    if (StringUtils.isBlank(id)) {
      return HttpResult.success();
    }
    return HttpResult.create(CommonStatus.OK, orgService.changeActive(id, active));
  }

  @ApiOperation("获取子机构列表分页")
  @GetMapping("/page")
  public HttpResult<PageInfo<SysOrg>> getPage(@ApiParam("分页参数") @PageBody PageRequest<SysOrg> page) {
    return HttpResult.success(orgService.getPage(page));
  }

  @ApiOperation("获取机构列表")
  @GetMapping("/list")
  public HttpResult<List<SysOrg>> getList(@ApiParam("条件") @RequestBody SysOrg condition,
                                          @ApiParam("是否多层级") Boolean multiLevel) {
    condition.setId(Checker.checkNotBlank(condition.getId(), JwtTokenManager.currentOrgId()));
    return HttpResult.success(orgService.getList(condition, null, null, multiLevel));
  }

  @ApiOperation("获取子机构")
  @GetMapping("/children")
  public HttpResult<List<SysOrg>> getChildren(@ApiParam("条件") @RequestBody SysOrg condition,
                                              @ApiParam("是否多层级") Boolean multiLevel) {
    condition.setParentId(Checker.checkNotBlank(condition.getParentId(), JwtTokenManager.currentOrgId()));
    condition.setId(null);
    return HttpResult.success(orgService.getList(condition, null, null, multiLevel));
  }

  @ApiOperation("获取组织机构树")
  @GetMapping("/tree")
  public HttpResult<List<SysOrg>> getOrgTree(@ApiParam("机构ID") String id,
                                             @ApiParam("是否可用") Boolean active) {
    if (StringUtils.isBlank(id)) {
      return HttpResult.failure("机构id不能为空");
    }
    return HttpResult.success(orgService.getOrgTreeList(id, active));
  }

}
