package com.benefitj.system;

import com.alibaba.fastjson2.JSON;
import com.benefitj.core.DateFmtter;
import com.benefitj.core.EventLoop;
import com.benefitj.scaffold.quartz.QuartzJobTaskService;
import com.benefitj.scaffold.quartz.entity.SysJob;
import com.benefitj.spring.quartz.JobWorker;
import com.benefitj.spring.quartz.QuartzJob;
import com.benefitj.spring.swagger.EnableSwaggerApi;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 系统管理
 */
@Slf4j
@PropertySource(value = "classpath:swagger-api-info.properties", encoding = "UTF-8")
@EnableSwaggerApi
@SpringBootApplication
public class SystemApplication {
  public static void main(String[] args) {
    SpringApplication.run(SystemApplication.class, args);
  }


  /**
   * 测试 Quartz 的 JobWorker
   */
  @Slf4j
  @Component("jobTaskWorker")
  public static class JobTaskWorker implements JobWorker {

    @Autowired
    private QuartzJobTaskService taskService;

    @Override
    public void execute(JobExecutionContext context, JobDetail detail, QuartzJob job) throws JobExecutionException {
      try {
        SysJob task = taskService.get(job.getId());
        log.info("\n------------------------------------- "
                + "\ntask: {}"
                + "\nnow: {}"
                + "\nkey: {}"
                + "\njobClass: {}"
                + "\njobDataMap: {} "
                + "\ndescription: {}"
                + "\nrefireCount: {}"
                + "\nnextFireTime: {}"
                + "\nthread: {}"
                + "\n-------------------------------------\n"
            , JSON.toJSONString(task)
            , DateFmtter.fmtNowS()
            , detail.getKey()
            , detail.getJobClass()
            , JSON.toJSONString(detail.getJobDataMap())
            , detail.getDescription()
            , context.getRefireCount()
            , DateFmtter.fmtS(context.getNextFireTime())
            , EventLoop.threadName());
      } catch (Exception e) {
        log.error("throws: " + e.getMessage(), e);
        //throw new JobExecutionException(e);
      }
    }
  }
}
