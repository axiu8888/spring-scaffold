package com.benefitj.system;

import com.alibaba.fastjson.JSON;
import com.benefitj.core.DateFmtter;
import com.benefitj.core.EventLoop;
import com.benefitj.scaffold.quartz.QuartzJobTaskService;
import com.benefitj.scaffold.quartz.entity.QuartzJobTaskEntity;
import com.benefitj.scaffold.security.token.JwtProperty;
import com.benefitj.spring.ctx.SpringCtxHolder;
import com.benefitj.spring.listener.AppStateHook;
import com.benefitj.spring.quartz.JobWorker;
import com.benefitj.spring.swagger.EnableSwaggerApi;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 系统管理
 */
@PropertySource(value = "classpath:swagger-api-info.properties", encoding = "UTF-8")
@EnableSwaggerApi
@SpringBootApplication
public class SystemApplication {
  public static void main(String[] args) {
    SpringApplication.run(SystemApplication.class, args);
  }

  private static final Logger log = LoggerFactory.getLogger(SystemApplication.class);

  static {
    AppStateHook.registerStart(e ->
        EventLoop.io().schedule(() -> {
          JwtProperty jwtProperty = SpringCtxHolder.getBean(JwtProperty.class);
          log.info("\n----- SIGNING_KEY -----------------------\n{}\n----- SIGNING_KEY -----------------------", jwtProperty.getSigningKey());
        }, 3, TimeUnit.SECONDS));
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
    public void execute(JobExecutionContext context, JobDetail detail, String taskId) {
      try {
        QuartzJobTaskEntity task = taskService.get(taskId);
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
            , fmtS(context.getNextFireTime())
            , EventLoop.threadName());
      } catch (Exception e) {
        log.error("throws: " + e.getMessage(), e);
        //throw new JobExecutionException(e);
      }
    }
  }
}
