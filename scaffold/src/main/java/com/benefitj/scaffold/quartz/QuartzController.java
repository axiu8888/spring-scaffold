package com.benefitj.scaffold.quartz;

import com.benefitj.scaffold.quartz.entity.QuartzJobTaskEntity;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.web.AopWebPointCut;
import com.benefitj.spring.mvc.query.PageBody;
import com.benefitj.spring.mvc.query.PageRequest;
import com.benefitj.spring.quartz.JobType;
import com.benefitj.spring.quartz.TriggerType;
import com.benefitj.spring.quartz.WorkerType;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Quartz的调度任务
 */
@Api(tags = {"调度任务"}, description = "Quartz的调度任务")
@AopWebPointCut
@RestController
@RequestMapping("/quartz")
public class QuartzController {

  @Autowired
  private QuartzJobTaskService quartzService;

  @ApiOperation("获取触发器类型")
  @GetMapping("/triggerType")
  public HttpResult<TriggerType[]> getTriggerType() {
    return HttpResult.success(TriggerType.values());
  }

  @ApiOperation("获取Job类型")
  @GetMapping("/jobType")
  public HttpResult<JobType[]> getJobType() {
    return HttpResult.success(JobType.values());
  }

  @ApiOperation("获取Worker类型")
  @GetMapping("/workerType")
  public HttpResult<WorkerType[]> getWorkerType() {
    return HttpResult.success(WorkerType.values());
  }

  @ApiOperation("获取Cron调度任务")
  @GetMapping
  public HttpResult<QuartzJobTaskEntity> get(@ApiParam("Cron调度任务的ID") String id) {
    return HttpResult.create(CommonStatus.OK, quartzService.get(id));
  }

  @ApiOperation("添加任务调度")
  @PostMapping
  public HttpResult<QuartzJobTaskEntity> create(@ApiParam("调度数据") QuartzJobTaskEntity task) {
    return HttpResult.create(CommonStatus.CREATED, quartzService.create(task));
  }

  @ApiOperation("更新任务调度")
  @PutMapping
  public HttpResult<QuartzJobTaskEntity> update(@ApiParam("任务调度数据") @RequestBody QuartzJobTaskEntity task) {
    if (StringUtils.isBlank(task.getId())) {
      return HttpResult.failure("任务调度任务的ID不能为空");
    }
    return HttpResult.create(CommonStatus.CREATED, quartzService.update(task));
  }

  @ApiOperation("删除任务调度")
  @DeleteMapping
  public HttpResult<Integer> delete(@ApiParam("任务调度ID") String id,
                                    @ApiParam("是否强制") Boolean force) {
    return HttpResult.create(CommonStatus.NO_CONTENT, quartzService.delete(id, Boolean.TRUE.equals(force)));
  }

  @ApiOperation("改变任务调度的状态")
  @PatchMapping("/active")
  public HttpResult<Boolean> changeActive(@ApiParam("任务调度ID") String id,
                                          @ApiParam("是否强制") Boolean active) {
    if (StringUtils.isBlank(id)) {
      return HttpResult.failure("调度任务的ID不能为空");
    }
    return HttpResult.create(CommonStatus.OK, quartzService.changeActive(id, active));
  }

  @ApiOperation("获取任务调度列表分页")
  @GetMapping("/page")
  public HttpResult<PageInfo<QuartzJobTaskEntity>> getPage(@ApiParam("分页参数") @PageBody PageRequest<QuartzJobTaskEntity> page) {
    PageInfo<QuartzJobTaskEntity> pageList = quartzService.getPage(page);
    return HttpResult.success(pageList);
  }

  @ApiOperation("获取机构的任务调度列表")
  @GetMapping("/list")
  public HttpResult<List<QuartzJobTaskEntity>> getJobTaskList(@ApiParam("条件") @RequestBody QuartzJobTaskEntity condition) {
    return HttpResult.success(quartzService.getList(condition, null, null, false));
  }


  @ApiModel("Worker方法")
  @Setter
  @Getter
  public static class WorkerMethodVo {
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;
    /**
     * 参数
     */
    @ApiModelProperty("参数")
    private List<WorkerParameter> params = new ArrayList<>();
    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

  }

  @ApiModel("Worker参数")
  @NoArgsConstructor
  @AllArgsConstructor
  @Setter
  @Getter
  public static class WorkerParameter {
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;
    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;
    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

  }

}
