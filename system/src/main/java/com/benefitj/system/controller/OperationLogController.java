package com.benefitj.system.controller;

import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.web.AopWebPointCut;
import com.benefitj.spring.mvc.query.PageBody;
import com.benefitj.spring.mvc.query.PageRequest;
import com.benefitj.spring.security.jwt.token.JwtTokenManager;
import com.benefitj.system.model.SysOperationLog;
import com.benefitj.system.service.SysOperationLogService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色
 */
@Api(tags = {"操作日志"}, description = "对日志的各种操作")
@AopWebPointCut
@RestController
@RequestMapping("/logs")
public class OperationLogController {

  @Autowired
  SysOperationLogService operationLogService;

  @ApiOperation("获取操作日志")
  @GetMapping
  public HttpResult<SysOperationLog> get(@ApiParam("日志ID") String id) {
    return HttpResult.create(CommonStatus.OK, operationLogService.getByPK(id));
  }

//  @ApiOperation("删除操作日志")
//  @ApiImplicitParams({
//      @ApiImplicitParam(name = "id", value = "日志ID", dataType = "String"),
//      @ApiImplicitParam(name = "force", value = "是否强制", dataType = "Boolean"),
//  })
//  @DeleteMapping
//  public HttpResult<Integer> delete(String id, Boolean force) {
//    int count = operationLogService.delete(id, Boolean.TRUE.equals(force));
//    return HttpResult.create(CommonStatus.NO_CONTENT, count);
//  }

  @ApiOperation("获取操作日志列表分页")
  @GetMapping("/page")
  public HttpResult<PageInfo<SysOperationLog>> getPage(@ApiParam("分页条件") @PageBody PageRequest<SysOperationLog> page) {
    return HttpResult.success(operationLogService.getPage(page));
  }

  @ApiOperation("获取机构的操作日志列表")
  @GetMapping("/list")
  public HttpResult<List<SysOperationLog>> getList(@ApiParam("条件") @RequestBody SysOperationLog condition,
                                                   @ApiParam("是否返回多级机构的数据") Boolean multiLevel) {
    condition.setOrgId(Checker.checkNotBlank(condition.getOrgId(), JwtTokenManager.currentOrgId()));
    if (StringUtils.isBlank(condition.getOrgId())) {
      return HttpResult.failure("orgId为空");
    }
    return HttpResult.success(operationLogService.getList(condition, null, null, multiLevel));
  }

}
