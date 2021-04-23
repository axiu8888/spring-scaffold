package com.benefitj.scaffold.quartz.listener;

import com.benefitj.core.EventLoop;
import com.benefitj.core.ReflectUtils;
import com.benefitj.scaffold.quartz.QuartzJobTaskService;
import com.benefitj.scaffold.quartz.entity.QuartzJobTaskEntity;
import com.benefitj.spring.quartz.QuartzUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 启动调度程序
 */
public class QuartzStartListener {

  private SchedulerFactoryBean schedulerFactoryBean;
  private QuartzJobTaskService service;

  /**
   * 是否启动
   */
  @Value("#{@environment['spring.quartz.task.start-up'] ?: true}")
  private boolean startup;

  public QuartzStartListener() {
  }

  public QuartzStartListener(SchedulerFactoryBean schedulerFactoryBean,
                             QuartzJobTaskService service) {
    this.schedulerFactoryBean = schedulerFactoryBean;
    this.service = service;
  }

  @EventListener
  public void onApplicationReadyEvent(ApplicationReadyEvent event) {
    if (startup) {
      Class<?> type = schedulerFactoryBean.getClass();
      Field field = ReflectUtils.getField(type, "startupDelay");
      if (field != null) {
        Integer value = ReflectUtils.getFieldValue(field, schedulerFactoryBean);
        // 调度任务
        EventLoop.io().schedule(() -> scheduleJobTasks(getService())
            , value + 3000
            , TimeUnit.MILLISECONDS);
      } else {
        // 调度任务
        EventLoop.io().schedule(() -> scheduleJobTasks(getService())
            , 5
            , TimeUnit.SECONDS);
      }
    }
  }

  public void scheduleJobTasks(QuartzJobTaskService service) {
    QuartzJobTaskEntity condition = new QuartzJobTaskEntity();
    condition.setActive(Boolean.TRUE);
    List<QuartzJobTaskEntity> all = service.getAll(condition);
    for (QuartzJobTaskEntity task : all) {
      QuartzUtils.scheduleJob(service.getScheduler(), task);
    }
  }

  public SchedulerFactoryBean getSchedulerFactoryBean() {
    return schedulerFactoryBean;
  }

  public void setSchedulerFactoryBean(SchedulerFactoryBean schedulerFactoryBean) {
    this.schedulerFactoryBean = schedulerFactoryBean;
  }

  public QuartzJobTaskService getService() {
    return service;
  }

  public void setService(QuartzJobTaskService service) {
    this.service = service;
  }
}

