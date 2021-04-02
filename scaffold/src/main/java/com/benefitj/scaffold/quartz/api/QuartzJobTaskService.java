package com.benefitj.scaffold.quartz.api;

import com.benefitj.core.IdUtils;
import com.benefitj.core.SingletonSupplier;
import com.benefitj.scaffold.BaseService;
import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.LogicException;
import com.benefitj.scaffold.page.PageableRequest;
import com.benefitj.scaffold.quartz.entity.QuartzJobTaskEntity;
import com.benefitj.scaffold.quartz.mapper.QuartzJobTaskMapper;
import com.benefitj.spring.BeanHelper;
import com.benefitj.spring.ctx.SpringCtxHolder;
import com.benefitj.spring.quartz.*;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Quartz调度任务
 */
@Service
public class QuartzJobTaskService extends BaseService<QuartzJobTaskEntity, QuartzJobTaskMapper> {

  private final SingletonSupplier<QuartzScheduler> QuartzSchedulerInstance = SingletonSupplier.of(this::wrapScheduler);

  @Autowired
  private QuartzJobTaskMapper mapper;

  public QuartzJobTaskService() {
  }

  @Override
  protected QuartzJobTaskMapper getMapper() {
    return mapper;
  }

  private QuartzScheduler wrapScheduler() {
    Scheduler s = SpringCtxHolder.getBean(Scheduler.class);
    if (s instanceof QuartzScheduler) {
      return (QuartzScheduler) s;
    }
    return new QuartzSchedulerWrapper(s);
  }

  /**
   * 获取调度器
   */
  public QuartzScheduler getScheduler() {
    return QuartzSchedulerInstance.get();
  }

  /**
   * 获取 job name
   */
  private String nextJobName() {
    String name = null;
    for (int i = 0; i < 1000; i++) {
      name = IdUtils.nextId(8);
      if (countJobName(name) <= 0) {
        break;
      }
    }
    if (StringUtils.isBlank(name)) {
      throw new LogicException("无法获取jobName");
    }
    return name;
  }


  /**
   * 统计 job name 出现的次数
   *
   * @param jobName job name
   * @return 返回出现的次数
   */
  public int countJobName(String jobName) {
    return getMapper().countJobName(jobName);
  }

  /**
   * 通过ID获取Cron的调度任务
   *
   * @param id 调度任务的ID
   * @return 返回调度任务
   */
  public QuartzJobTaskEntity get(String id) {
    return getByPK(id);
  }

  /**
   * 创建Cron调度任务
   *
   * @param task 调度任务
   */
  public QuartzJobTaskEntity create(QuartzJobTaskEntity task) {
    if (StringUtils.isBlank(task.getDescription())) {
      throw new LogicException("请给此调度任务添加一个描述");
    }

    // 任务ID
    task.setId(IdUtils.uuid());
    // 创建时间
    task.setCreateTime(new Date());
    task.setActive(Boolean.TRUE);
    setupJobTask(task);
    QuartzUtils.scheduleJob(getScheduler(), task);
    // 保存
    super.insert(task);
    return task;
  }

  private void setupJobTask(QuartzJobTaskEntity task) {
    TriggerType triggerType = TriggerType.of(task.getTriggerType());
    if (triggerType == null) {
      throw new LogicException("请指定正确的触发器类型");
    }

    QuartzUtils.checkWorker(task);

    String jobName = nextJobName();
    // 触发器组名称
    task.setTriggerGroup(triggerType.name());
    // 创建随机的触发器名称
    task.setTriggerName("trigger-" + jobName);
    // Job组名称
    task.setJobGroup(triggerType.name());
    // 创建随机的 JobName
    task.setJobName(jobName);

    // 开始时间
    long now = System.currentTimeMillis();
    Checker.checkNull(task.getStartAt(), () -> task.setStartAt(now));

    // 调度的时间不能比当前时间更早
    if (task.getStartAt() < now) {
      task.setStartAt(now);
    }

    if (triggerType == TriggerType.CRON) {
      try {
        // 验证表达式
        CronExpression.validateExpression(task.getCronExpression());
      } catch (ParseException e) {
        throw new LogicException("[" + task.getCronExpression() + "]表达式错误: " + e.getMessage());
      }
      if (task.getMisfirePolicy() == null) {
        // 默认什么都不做
        task.setMisfirePolicy(TriggerType.CronPolicy.DO_NOTHING.getPolicy());
      }
    } else {
      // 验证Simple的值
      // 执行次数
      Checker.checkNull(task.getSimpleRepeatCount(), () -> task.setSimpleRepeatCount(0));
      // 间隔时间
      Checker.checkNull(task.getSimpleInterval(), () -> task.setSimpleInterval(0L));

      if (task.getMisfirePolicy() == null) {
        // 默认什么都不做
        task.setMisfirePolicy(TriggerType.SimplePolicy.SMART_POLICY.getPolicy());
      }
    }

    if (task.getEndAt() != null) {
      // 至少开始后的5秒再结束
      task.setEndAt(Math.max(task.getStartAt() + 5_000, task.getEndAt()));
    }

    Checker.checkNull(task.getAsync(), () -> task.setAsync(false));
    Checker.checkNull(task.getRecovery(), () -> task.setRecovery(false));
    Checker.checkNull(task.getPersistent(), () -> task.setPersistent(false));
    Checker.checkNull(task.getDisallowConcurrent(), () -> task.setDisallowConcurrent(false));
    Checker.checkNull(task.getPriority(), () -> task.setPriority(QuartzJobTaskEntity.TRIGGER_PRIORITY));

    // job类型
    JobType jobType = JobType.of(task.getJobType());
    // 任务类型
    task.setJobType(jobType.name());
    // 是否持久化
    task.setPersistent(jobType.isPersistent());
    // 不允许并发执行
    task.setDisallowConcurrent(jobType.isDisallowConcurrent());
  }

  /**
   * 更新调度任务
   *
   * @param task 调度任务
   * @return 返回
   */
  @Transactional(rollbackFor = Exception.class)
  public QuartzJobTaskEntity update(QuartzJobTaskEntity task) {
    QuartzJobTaskEntity existTask = get(task.getId());
    if (existTask != null) {
      // 触发器组和名称、Job组合名称 都为自动生成，触发器类型必须指定

      TriggerType type = TriggerType.of(task.getTriggerType());
      if (type == null) {
        throw new LogicException("请指定正确的触发器类型");
      }

      if (!type.name().equalsIgnoreCase(existTask.getTriggerType())) {
        throw new LogicException("无法修改触发器类型");
      }

      QuartzJobTaskEntity copy = BeanHelper.copy(existTask, QuartzJobTaskEntity.class);
      BeanHelper.copy(task, existTask);
      existTask.setCreateTime(copy.getCreateTime());
      existTask.setUpdateTime(new Date());
      existTask.setActive(copy.getActive());

      // 重置调度
      setupJobTask(existTask);

      try {
        Scheduler s = getScheduler();
        // 停止任务和触发器
        // 删除存在的job
        s.pauseTrigger(QuartzUtils.triggerKey(copy));
        s.deleteJob(QuartzUtils.jobKey(copy));
        // 重新调度
        QuartzUtils.scheduleJob(s, existTask);
        updateByPK(existTask);
        return existTask;
      } catch (Exception e) {
        getScheduler().resumeTrigger(QuartzUtils.triggerKey(existTask));
        throw new IllegalStateException(e);
      }
    }
    return existTask;
  }

  /**
   * 删除调度的任务
   *
   * @param id    任务的ID
   * @param force 是否强制删除(暂不起作用)
   * @return 返回删除的条数(0或1)
   */
  @Transactional(rollbackFor = Exception.class)
  public int delete(String id, boolean force) {
    QuartzJobTaskEntity task = get(id);
    if (task != null) {
      QuartzScheduler sched = getScheduler();
      sched.pauseTrigger(QuartzUtils.triggerKey(task));
      sched.deleteJob(QuartzUtils.jobKey(task));
      return deleteByPK(id);
    }
    return 0;
  }

  /**
   * 改变 Job 的状态，暂停或执行
   *
   * @param id     任务ID
   * @param active 状态
   * @return 返回状态是否改变
   */
  @Transactional(rollbackFor = Exception.class)
  public boolean changeActive(String id, Boolean active) {
    final QuartzJobTaskEntity task = get(id);
    if (task != null) {
      task.setActive(Boolean.TRUE.equals(active));
      task.setUpdateTime(new Date());

      QuartzScheduler sched = getScheduler();
      JobKey jobKey = QuartzUtils.jobKey(task);
      if (Boolean.TRUE.equals(active)) {
        QuartzUtils.scheduleJob(sched, task);
      } else {
        sched.pauseJob(jobKey);
      }
      return getMapper().updateByPrimaryKeySelective(task) > 0;
    }
    return false;
  }

  @Override
  public List<QuartzJobTaskEntity> getList(QuartzJobTaskEntity condition, Date startTime, Date endTime, boolean multiLevel) {
    return getMapper().selectList(condition, startTime, endTime, multiLevel);
  }

  @Override
  public PageInfo<QuartzJobTaskEntity> getPage(PageableRequest<QuartzJobTaskEntity> page) {
    return super.getPage(page);
  }

  public List<QuartzJobTaskEntity> getAll() {
    return getMapper().selectAll();
  }

  public void scheduleJobTasks(List<QuartzJobTaskEntity> tasks) {
    tasks.stream()
        .filter(t -> countByPK(t.getId()) > 0)
        .filter(t -> Boolean.TRUE.equals(t.getActive()))
        .forEach(t -> QuartzUtils.scheduleJob(getScheduler(), t));
  }

}
