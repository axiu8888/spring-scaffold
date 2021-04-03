package com.benefitj.scaffold.quartz;

import com.benefitj.core.EventLoop;
import com.benefitj.core.ReflectUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 * 启动调度程序
 */
public class QuartzStartListener {

  private SchedulerFactoryBean schedulerFactoryBean;
  private QuartzJobTaskService service;

  public QuartzStartListener() {
  }

  public QuartzStartListener(SchedulerFactoryBean schedulerFactoryBean,
                             QuartzJobTaskService service) {
    this.schedulerFactoryBean = schedulerFactoryBean;
    this.service = service;
  }

  @EventListener
  public void onApplicationReadyEvent(ApplicationReadyEvent event) {
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

  public void scheduleJobTasks(QuartzJobTaskService service) {
    service.scheduleJobTasks(service.getAll());
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

