package com.benefitj.quartz;

import com.benefitj.core.EventLoop;
import com.benefitj.core.ReflectUtils;
import com.benefitj.spring.applicationevent.IApplicationReadyEventListener;
import com.benefitj.quartz.service.QrtzJobTaskService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 * 启动调度程序
 */
public class QuartzStartListener implements IApplicationReadyEventListener {

  private SchedulerFactoryBean schedulerFactoryBean;
  private QrtzJobTaskService service;

  public QuartzStartListener() {
  }

  public QuartzStartListener(SchedulerFactoryBean schedulerFactoryBean,
                             QrtzJobTaskService service) {
    this.schedulerFactoryBean = schedulerFactoryBean;
    this.service = service;
  }

  @Override
  public void onApplicationReadyEvent(ApplicationReadyEvent event) {
    Class<?> type = schedulerFactoryBean.getClass();
    Field field = ReflectUtils.getField(type, "startupDelay");
    if (field != null) {
      Integer value = ReflectUtils.getFieldValue(field, schedulerFactoryBean);
      // 调度任务
      EventLoop.multi().schedule(() -> scheduleJobTasks(getService())
          , value + 3000
          , TimeUnit.MILLISECONDS);
    } else {
      // 调度任务
      EventLoop.multi().schedule(() -> scheduleJobTasks(getService())
          , 5
          , TimeUnit.SECONDS);
    }
  }

  public void scheduleJobTasks(QrtzJobTaskService service) {
    service.scheduleJobTasks(service.getAll());
  }

  public SchedulerFactoryBean getSchedulerFactoryBean() {
    return schedulerFactoryBean;
  }

  public void setSchedulerFactoryBean(SchedulerFactoryBean schedulerFactoryBean) {
    this.schedulerFactoryBean = schedulerFactoryBean;
  }

  public QrtzJobTaskService getService() {
    return service;
  }

  public void setService(QrtzJobTaskService service) {
    this.service = service;
  }
}

