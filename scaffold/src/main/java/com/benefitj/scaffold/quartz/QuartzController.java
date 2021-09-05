package com.benefitj.scaffold.quartz;

import com.benefitj.scaffold.quartz.entity.QuartzJobTaskEntity;
import com.benefitj.scaffold.quartz.pin.PinParam;
import com.benefitj.scaffold.quartz.pin.PinManager;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.web.AopWebPointCut;
import com.benefitj.spring.mvc.page.PageableRequest;
import com.benefitj.spring.mvc.request.GetBody;
import com.benefitj.spring.mvc.request.PageBody;
import com.benefitj.spring.quartz.JobType;
import com.benefitj.spring.quartz.TriggerType;
import com.benefitj.spring.quartz.WorkerType;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Quartz的调度任务
 */
@AopWebPointCut
@Api(tags = {"调度任务"}, description = "Quartz的调度任务")
@RestController
@RequestMapping("/quartz")
public class QuartzController {

  @Autowired
  private QuartzJobTaskService quartzService;
  @Autowired
  private PinManager pinManager;

  @ApiOperation("获取触发器类型")
  @GetMapping("/triggerType")
  public HttpResult<?> getTriggerType() {
    return HttpResult.success(TriggerType.values());
  }

  @ApiOperation("获取Job类型")
  @GetMapping("/jobType")
  public HttpResult<?> getJobType() {
    return HttpResult.success(JobType.values());
  }

  @ApiOperation("获取Worker类型")
  @GetMapping("/workerType")
  public HttpResult<?> getWorkerType() {
    return HttpResult.success(WorkerType.values());
  }

  @ApiOperation("获取Cron调度任务")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "Cron调度任务的ID", required = true, dataType = "String", dataTypeClass = String.class),
  })
  @GetMapping
  public HttpResult<?> get(String id) {
    QuartzJobTaskEntity task = quartzService.get(id);
    return HttpResult.create(CommonStatus.OK, task);
  }

  @ApiOperation("添加任务调度")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "task", dataTypeClass = QuartzJobTaskEntity.class),
  })
  @PostMapping
  public HttpResult<?> create(QuartzJobTaskEntity task) {
    task = quartzService.create(task);
    return HttpResult.create(CommonStatus.CREATED, task);
  }

  @ApiOperation("更新任务调度")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "task", value = "任务调度数据", dataTypeClass = QuartzJobTaskEntity.class),
  })
  @PutMapping
  public HttpResult<?> update(@RequestBody QuartzJobTaskEntity task) {
    if (StringUtils.isBlank(task.getId())) {
      return HttpResult.failure("任务调度任务的ID不能为空");
    }
    task = quartzService.update(task);
    return HttpResult.create(CommonStatus.CREATED, task);
  }

  @ApiOperation("删除任务调度")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "任务调度ID", dataType = "String", dataTypeClass = String.class),
      @ApiImplicitParam(name = "force", value = "是否强制", dataType = "Boolean", dataTypeClass = Boolean.class),
  })
  @DeleteMapping
  public HttpResult<?> delete(String id, Boolean force) {
    int count = quartzService.delete(id, Boolean.TRUE.equals(force));
    return HttpResult.create(CommonStatus.NO_CONTENT, count);
  }

  @ApiOperation("改变任务调度的状态")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "任务调度ID", dataType = "String", paramType = "form", dataTypeClass = String.class),
      @ApiImplicitParam(name = "active", value = "状态", dataType = "Boolean", paramType = "form", dataTypeClass = Boolean.class),
  })
  @PatchMapping("/active")
  public HttpResult<?> changeActive(String id, Boolean active) {
    if (StringUtils.isBlank(id)) {
      return HttpResult.failure("调度任务的ID不能为空");
    }
    Boolean result = quartzService.changeActive(id, active);
    return HttpResult.create(CommonStatus.OK, result);
  }

  @ApiOperation("获取任务调度列表分页")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "page", value = "分页参数", dataType = "RequestPage", dataTypeClass = PageableRequest.class),
  })
  @GetMapping("/page")
  public HttpResult<?> getPage(@PageBody PageableRequest<QuartzJobTaskEntity> page) {
    PageInfo<QuartzJobTaskEntity> pageList = quartzService.getPage(page);
    return HttpResult.success(pageList);
  }

  @ApiOperation("获取机构的任务调度列表")
  @ApiImplicitParams({})
  @GetMapping("/list")
  public HttpResult<?> getJobTaskList(@GetBody QuartzJobTaskEntity condition) {
    List<QuartzJobTaskEntity> all = quartzService.getList(condition, null, null, false);
    return HttpResult.success(all);
  }

  @ApiOperation("获取对外发布的调度服务列表")
  @ApiImplicitParams({})
  @GetMapping("/workerPin/list")
  public HttpResult<?> getWorkerPinList() {
    return HttpResult.success(pinManager.values()
        .stream()
        .map(wm -> {
          WorkerMethodVo vo = new WorkerMethodVo();
          vo.setName(wm.getPinName());
          vo.setDescription(wm.getPinDescription());
          for (PinParam param : wm.getPinParams()) {
            vo.getParams()
                .add(new WorkerParameter(param.name(), param.type().getName(), param.description()));
          }
          return vo;
        })
        .collect(Collectors.toList()));
  }

  @Setter
  @Getter
  public static class WorkerMethodVo {
    /**
     * 名称
     */
    private String name;
    /**
     * 参数
     */
    private List<WorkerParameter> params = new ArrayList<>();
    /**
     * 描述
     */
    private String description;

  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Setter
  @Getter
  public static class WorkerParameter {
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 描述
     */
    private String description;

  }

}
