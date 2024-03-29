package com.benefitj.scaffold.quartz;

import com.benefitj.core.IdUtils;
import com.benefitj.scaffold.BaseService;
import com.benefitj.scaffold.quartz.entity.SysJob;
import com.benefitj.scaffold.quartz.mapper.QuartzJobTaskMapper;
import com.benefitj.spring.BeanHelper;
import com.benefitj.spring.quartz.IScheduler;
import com.benefitj.spring.quartz.QuartzException;
import com.benefitj.spring.quartz.QuartzUtils;
import com.benefitj.spring.quartz.TriggerType;
import com.benefitj.spring.security.jwt.token.JwtTokenManager;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Quartz调度任务
 */
@Service
public class QuartzJobTaskService extends BaseService<SysJob, QuartzJobTaskMapper> {

  @Autowired
  QuartzJobTaskMapper mapper;

  @Autowired
  IScheduler scheduler;

  public QuartzJobTaskService() {
  }

  @Override
  protected QuartzJobTaskMapper getMapper() {
    return mapper;
  }


  /**
   * 获取调度器
   */
  public IScheduler getScheduler() {
    return scheduler;
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
      throw new QuartzException("无法获取jobName");
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
  public SysJob get(String id) {
    return getByPK(id);
  }

  /**
   * 创建Cron调度任务
   *
   * @param task 调度任务
   */
  public SysJob create(SysJob task) {
    if (StringUtils.isBlank(task.getDescription())) {
      throw new QuartzException("请给此调度任务添加一个描述");
    }

    // 任务ID
    task.setId(IdUtils.uuid());
    // 机构ID
    if (StringUtils.isBlank(task.getOrgId())) {
      task.setOrgId(JwtTokenManager.currentOrgId());
    }
    // 创建时间
    task.setCreateTime(new Date());
    task.setActive(Boolean.TRUE);
    QuartzUtils.setup(task, nextJobName());
    QuartzUtils.scheduleJob(getScheduler(), task);
    // 保存
    super.insert(task);
    return task;
  }

  /**
   * 更新调度任务
   *
   * @param task 调度任务
   * @return 返回
   */
  @Transactional(rollbackFor = Exception.class)
  public SysJob update(SysJob task) {
    SysJob existTask = get(task.getId());
    if (existTask != null) {
      // 触发器组和名称、Job组合名称 都为自动生成，触发器类型必须指定

      TriggerType type = task.getTriggerType();
      if (type == null) {
        throw new QuartzException("请指定正确的触发器类型");
      }

      if (type != existTask.getTriggerType()) {
        throw new QuartzException("无法修改触发器类型");
      }

      SysJob copy = BeanHelper.copy(existTask, SysJob.class);
      BeanHelper.copy(task, existTask);
      existTask.setCreateTime(copy.getCreateTime());
      existTask.setUpdateTime(new Date());
      existTask.setActive(copy.getActive());

      // 重置调度
      QuartzUtils.setup(existTask, nextJobName());

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
    SysJob task = get(id);
    if (task != null) {
      IScheduler sched = getScheduler();
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
    final SysJob task = get(id);
    if (task != null) {
      task.setActive(Boolean.TRUE.equals(active));
      task.setUpdateTime(new Date());

      IScheduler sched = getScheduler();
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
  public List<SysJob> getList(SysJob condition, Date startTime, Date endTime, Boolean multiLevel) {
    if (StringUtils.isBlank(condition.getOrgId())) {
      condition.setOrgId(JwtTokenManager.currentOrgId());
    }
    return getMapper().selectList(condition, startTime, endTime, multiLevel);
  }

}
