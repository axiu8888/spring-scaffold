package com.benefitj.quartz.job;

import com.benefitj.spring.ctx.SpringCtxHolder;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 调用 job task
 */
public class JobTaskCaller implements Job {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  public JobTaskCaller() {
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    try {
      JobDetail detail = context.getJobDetail();
      JobDataMap jobDataMap = detail.getJobDataMap();
      String taskId = jobDataMap.getString(JobWorker.KEY_ID);
      String worker = jobDataMap.getString(JobWorker.KEY_WORKER);

      WorkerType workerType = WorkerType.of(jobDataMap.getString(JobWorker.KEY_WORKER_TYPE));
      Object jobWorker = null;
      switch (workerType) {
        case NEW_INSTANCE:
          jobWorker = newJobWorkerInstance(classForName(worker));
          break;
        case SPRING_BEAN_NAME:
          jobWorker = getBean(worker);
          break;
        case SPRING_BEAN_CLASS:
          jobWorker = getBean(classForName(worker));
          break;
      }
      if (jobWorker != null) {
        if (jobWorker instanceof JobWorker) {
          ((JobWorker) jobWorker).execute(context, detail, taskId);
        } else {
          logger.warn("Fail JobWorker instance: " + jobWorker.getClass());
        }
      } else {
        logger.warn("Not found JobWorker instance: " + worker);
      }
    } catch (Exception e) {
      logger.error("throws: " + e.getMessage(), e);
      throw new JobExecutionException(e);
    }
  }

  public Class<? extends JobWorker> classForName(String name) throws SchedulerException {
    try {
      return (Class<? extends JobWorker>) Class.forName(name);
    } catch (ClassNotFoundException e) {
      throw new SchedulerException(e.getMessage());
    }
  }

  /**
   * 创建 JobWorker 实例
   *
   * @param worker JobWorker类
   * @return 返回实例对象
   * @throws JobExecutionException
   */
  public Object newJobWorkerInstance(Class<?> worker) throws JobExecutionException {
    try {
      return worker.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new JobExecutionException(e);
    }
  }

  /**
   * 获取bean实例
   *
   * @param requiredType bean类型
   * @param <T>          类型
   * @return 返回实例
   */
  public <T> T getBean(Class<T> requiredType) {
    return SpringCtxHolder.getBean(requiredType);
  }

  /**
   * 获取bean实例
   *
   * @param name bean名称
   * @param <T>  类型
   * @return 返回实例
   */
  public <T> T getBean(String name) {
    return SpringCtxHolder.getBean(name);
  }

}
