package com.benefitj.system.controller;

import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.web.AopWebPointCut;
import com.benefitj.spring.mvc.query.PageBody;
import com.benefitj.spring.mvc.query.PageRequest;
import com.benefitj.spring.security.jwt.token.JwtTokenManager;
import com.benefitj.system.model.SysDictionaryModel;
import com.benefitj.system.service.SysDictionaryService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典
 */
@Api(tags = {"字典"}, description = "对字典的各种操作")
@AopWebPointCut
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

  @Autowired
  private SysDictionaryService dictionaryService;

  @ApiOperation("获取字典")
  @GetMapping
  public HttpResult<SysDictionaryModel> get(@ApiParam("ID") String id) {
    return HttpResult.create(CommonStatus.OK, dictionaryService.get(id));
  }

  @ApiOperation("添加字典")
  @PostMapping
  public HttpResult<SysDictionaryModel> create(@ApiParam("数据") SysDictionaryModel dictionary) {
    return HttpResult.create(CommonStatus.CREATED, dictionaryService.create(dictionary));
  }

  @ApiOperation("更新字典")
  @PutMapping
  public HttpResult<SysDictionaryModel> update(@ApiParam("数据") @RequestBody SysDictionaryModel dictionary) {
    if (StringUtils.isAnyBlank(dictionary.getId(), dictionary.getName())) {
      return HttpResult.failure("字典ID和字典名都不能为空");
    }
    return HttpResult.create(CommonStatus.CREATED, dictionaryService.update(dictionary));
  }

  @ApiOperation("删除字典")
  @DeleteMapping
  public HttpResult<Integer> delete(@ApiParam("ID") String id, @ApiParam("是否强制") Boolean force) {
    return HttpResult.create(CommonStatus.NO_CONTENT, dictionaryService.delete(id, Boolean.TRUE.equals(force)));
  }

  @ApiOperation("改变字典的状态")
  @PatchMapping("/active")
  public HttpResult<Boolean> changeActive(@ApiParam("ID") String id, @ApiParam("是否可用") Boolean active) {
    if (StringUtils.isBlank(id)) {
      return HttpResult.failure("字典ID不能为空");
    }
    return HttpResult.create(CommonStatus.OK, dictionaryService.changeActive(id, active));
  }

  @ApiOperation("获取字典列表分页")
  @GetMapping("/page")
  public HttpResult<PageInfo<SysDictionaryModel>> getPage(@ApiParam("分页") @PageBody PageRequest<SysDictionaryModel> page) {
    return HttpResult.success(dictionaryService.getPage(page));
  }

  @ApiOperation("获取机构的字典列表")
  @GetMapping("/list")
  public HttpResult<List<SysDictionaryModel>> getList(@ApiParam("条件") @RequestBody SysDictionaryModel condition,
                                                      @ApiParam("是否多层级") Boolean multiLevel) {
    condition.setOrgId(Checker.checkNotBlank(condition.getOrgId(), JwtTokenManager.currentOrgId()));
    if (StringUtils.isBlank(condition.getOrgId())) {
      return HttpResult.failure("orgId为空");
    }
    List<SysDictionaryModel> dictionaryList =
        dictionaryService.getList(condition, null, null, Boolean.TRUE.equals(multiLevel));
    return HttpResult.success(dictionaryList);
  }

}
