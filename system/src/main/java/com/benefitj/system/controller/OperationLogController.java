package com.benefitj.system.controller;

import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.security.token.JwtTokenManager;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.web.AopWebPointCut;
import com.benefitj.spring.mvc.page.PageableRequest;
import com.benefitj.spring.mvc.request.GetBody;
import com.benefitj.spring.mvc.request.PageBody;
import com.benefitj.system.model.SysOperationLog;
import com.benefitj.system.service.SysOperationLogService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色
 */
@AopWebPointCut
@Api(tags = {"操作日志"}, description = "对日志的各种操作")
@RestController
@RequestMapping("/logs")
public class OperationLogController {

  @Autowired
  private SysOperationLogService operationLogService;

  @ApiOperation("获取操作日志")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "日志ID", required = true, dataType = "String", dataTypeClass = String.class),
  })
  @GetMapping
  public HttpResult<?> get(String id) {
    SysOperationLog sol = operationLogService.getByPK(id);
    return HttpResult.create(CommonStatus.OK, sol);
  }

//  @ApiOperation("删除操作日志")
//  @ApiImplicitParams({
//      @ApiImplicitParam(name = "id", value = "日志ID", dataType = "String"),
//      @ApiImplicitParam(name = "force", value = "是否强制", dataType = "Boolean"),
//  })
//  @DeleteMapping
//  public HttpResult<?> delete(String id, Boolean force) {
//    int count = operationLogService.delete(id, Boolean.TRUE.equals(force));
//    return HttpResult.create(CommonStatus.NO_CONTENT, count);
//  }

  @ApiOperation("获取操作日志列表分页")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "page", value = "分页参数", dataType = "RequestPage", dataTypeClass = PageableRequest.class),
  })
  @GetMapping("/page")
  public HttpResult<?> getPage(@PageBody PageableRequest<SysOperationLog> page) {
    PageInfo<SysOperationLog> info = operationLogService.getPage(page);
    return HttpResult.success(info);
  }

  @ApiOperation("获取机构的操作日志列表")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "module", value = "日志模块", dataType = "String", dataTypeClass = String.class),
      @ApiImplicitParam(name = "operation", value = "操作", dataType = "String", dataTypeClass = String.class),
      @ApiImplicitParam(name = "orgId", value = "机构ID", dataType = "String", dataTypeClass = String.class),
      @ApiImplicitParam(name = "creatorId", value = "操作者ID", dataType = "String", dataTypeClass = String.class),
      @ApiImplicitParam(name = "url", value = "URL", dataType = "String", dataTypeClass = String.class),
      @ApiImplicitParam(name = "active", value = "是否可用", dataType = "Boolean", dataTypeClass = Boolean.class),
      @ApiImplicitParam(name = "multiLevel", value = "是否返回多级机构的数据", dataType = "Boolean", dataTypeClass = Boolean.class),
  })
  @GetMapping("/list")
  public HttpResult<?> getList(@GetBody SysOperationLog condition, Boolean multiLevel) {
    condition.setOrgId(Checker.checkNotBlank(condition.getOrgId(), JwtTokenManager.currentOrgId()));
    if (StringUtils.isBlank(condition.getOrgId())) {
      return HttpResult.failure("orgId为空");
    }
    List<SysOperationLog> list = operationLogService.getList(condition, null, null, multiLevel);
    return HttpResult.success(list);
  }

}
