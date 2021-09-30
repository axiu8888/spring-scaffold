package com.benefitj.system.controller;

import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.security.token.JwtTokenManager;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.web.AopWebPointCut;
import com.benefitj.spring.mvc.page.PageableRequest;
import com.benefitj.spring.mvc.request.GetBody;
import com.benefitj.spring.mvc.request.PageBody;
import com.benefitj.system.model.SysDictionaryModel;
import com.benefitj.system.service.SysDictionaryService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 字典
 */
@AopWebPointCut
@Api(tags = {"字典"}, description = "对字典的各种操作")
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

  @Autowired
  private SysDictionaryService dictionaryService;

  @ApiOperation("获取字典")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "字典ID", required = true, dataType = "String", dataTypeClass = String.class),
  })
  @GetMapping
  public HttpResult<?> get(String id) {
    SysDictionaryModel dictionary = dictionaryService.get(id);
    return HttpResult.create(CommonStatus.OK, dictionary);
  }

  @ApiOperation("添加字典")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "dictionary", value = "字典数据", dataType = "String", dataTypeClass = String.class),
  })
  @PostMapping
  public HttpResult<?> create(SysDictionaryModel dictionary) {
    dictionary = dictionaryService.create(dictionary);
    return HttpResult.create(CommonStatus.CREATED, dictionary);
  }

  @ApiOperation("更新字典")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "dictionary", value = "字典数据", dataType = "String", dataTypeClass = String.class),
  })
  @PutMapping
  public HttpResult<?> update(@RequestBody SysDictionaryModel dictionary) {
    if (StringUtils.isAnyBlank(dictionary.getId(), dictionary.getName())) {
      return HttpResult.failure("字典ID和字典名都不能为空");
    }
    dictionary = dictionaryService.update(dictionary);
    return HttpResult.create(CommonStatus.CREATED, dictionary);
  }

  @ApiOperation("删除字典")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "字典ID", dataType = "String", dataTypeClass = String.class),
      @ApiImplicitParam(name = "force", value = "是否强制", dataType = "Boolean", dataTypeClass = Boolean.class),
  })
  @DeleteMapping
  public HttpResult<?> delete(String id, Boolean force) {
    int count = dictionaryService.delete(id, Boolean.TRUE.equals(force));
    return HttpResult.create(CommonStatus.NO_CONTENT, count);
  }

  @ApiOperation("改变字典的状态")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "字典ID", dataType = "String", paramType = "form", dataTypeClass = String.class),
      @ApiImplicitParam(name = "active", value = "状态", dataType = "Boolean", paramType = "form", dataTypeClass = Boolean.class),
  })
  @PatchMapping("/active")
  public HttpResult<?> changeActive(String id, Boolean active) {
    if (StringUtils.isBlank(id)) {
      return HttpResult.failure("字典ID不能为空");
    }
    Boolean result = dictionaryService.changeActive(id, active);
    return HttpResult.create(CommonStatus.OK, result);
  }

  @ApiOperation("获取字典列表分页")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "page", value = "分页参数", dataType = "PageableRequest", dataTypeClass = PageableRequest.class),
//      @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "Integer", dataTypeClass = Integer.class),
//      @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "Integer", dataTypeClass = Integer.class),
//      @ApiImplicitParam(name = "orderBy", value = "排序", dataType = "String[]", dataTypeClass = String[].class),
//      @ApiImplicitParam(name = "active", value = "是否可用", dataType = "Boolean", dataTypeClass = Boolean.class),
//      @ApiImplicitParam(name = "multiLevel", value = "是否返回多级机构的数据", dataType = "Boolean", dataTypeClass = Boolean.class),
//      @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "Date", dataTypeClass = Date.class),
//      @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "Date", dataTypeClass = Date.class),
//      @ApiImplicitParam(name = "orgId", value = "机构ID", dataType = "String", dataTypeClass = String.class),
//      @ApiImplicitParam(name = "parentId", value = "父级节点", dataType = "String", dataTypeClass = String.class),
//      @ApiImplicitParam(name = "name", value = "字典名称", dataType = "String", dataTypeClass = String.class),
//      @ApiImplicitParam(name = "code", value = "字典代码(字符&唯一)", dataType = "String", dataTypeClass = String.class),
//      @ApiImplicitParam(name = "attribute", value = "字典属性", dataType = "String", dataTypeClass = String.class),
//      @ApiImplicitParam(name = "value", value = "字典属性值", dataType = "String", dataTypeClass = String.class),
  })
  @GetMapping("/page")
  public HttpResult<?> getPage(@PageBody PageableRequest<SysDictionaryModel> page) {
    PageInfo<SysDictionaryModel> pageInfo = dictionaryService.getPage(page);
    return HttpResult.success(pageInfo);
  }

  @ApiOperation("获取机构的字典列表")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "name", value = "字典名称", dataType = "String", dataTypeClass = String.class),
      @ApiImplicitParam(name = "orgId", value = "机构ID", dataType = "String", dataTypeClass = String.class),
      @ApiImplicitParam(name = "parentId", value = "父级节点", dataType = "String", dataTypeClass = String.class),
      @ApiImplicitParam(name = "code", value = "字典代码(字符&唯一)", dataType = "String", dataTypeClass = String.class),
      @ApiImplicitParam(name = "attribute", value = "字典属性", dataType = "String", dataTypeClass = String.class),
      @ApiImplicitParam(name = "value", value = "字典属性值", dataType = "String", dataTypeClass = String.class),
      @ApiImplicitParam(name = "active", value = "是否可用", dataType = "Boolean", dataTypeClass = Boolean.class),
      @ApiImplicitParam(name = "multiLevel", value = "是否返回多级机构的数据", dataType = "Boolean", dataTypeClass = Boolean.class),
  })
  @GetMapping("/list")
  public HttpResult<?> getList(@GetBody SysDictionaryModel condition, Boolean multiLevel) {
    condition.setOrgId(Checker.checkNotBlank(condition.getOrgId(), JwtTokenManager.currentOrgId()));
    if (StringUtils.isBlank(condition.getOrgId())) {
      return HttpResult.failure("orgId为空");
    }
    List<SysDictionaryModel> dictionaryList =
        dictionaryService.getList(condition, null, null, Boolean.TRUE.equals(multiLevel));
    return HttpResult.success(dictionaryList);
  }

}
