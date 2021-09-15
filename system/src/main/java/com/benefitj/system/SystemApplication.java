package com.benefitj.system;

import com.alibaba.fastjson.JSON;
import com.benefitj.core.DateFmtter;
import com.benefitj.core.EventLoop;
import com.benefitj.scaffold.quartz.QuartzJobTaskService;
import com.benefitj.scaffold.quartz.entity.QuartzJobTaskEntity;
import com.benefitj.scaffold.quartz.pin.ParamType;
import com.benefitj.scaffold.quartz.pin.Pin;
import com.benefitj.scaffold.quartz.pin.PinParam;
import com.benefitj.scaffold.security.token.JwtProperty;
import com.benefitj.spring.ctx.EnableSpringCtxInit;
import com.benefitj.spring.ctx.SpringCtxHolder;
import com.benefitj.spring.listener.AppStateHook;
import com.benefitj.spring.listener.EnableAppStateListener;
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
@EnableSpringCtxInit
@EnableAppStateListener
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
        System.err.println("\n-------------------------------------");
        System.err.println("task: " + JSON.toJSONString(task));
        System.err.println("now: " + DateFmtter.fmtNowS());
        System.err.println("key: " + detail.getKey());
        System.err.println("jobClass: " + detail.getJobClass());
        System.err.println("jobDataMap: " + JSON.toJSONString(detail.getJobDataMap()));
        System.err.println("description: " + detail.getDescription());
        System.err.println("refireCount: " + context.getRefireCount());
        System.err.println("nextFireTime: " + fmtS(context.getNextFireTime()));
        System.err.println("thread: " + Thread.currentThread().getName());
        System.err.println("-------------------------------------\n");
      } catch (Exception e) {
        log.error("throws: " + e.getMessage(), e);
        //throw new JobExecutionException(e);
      }
    }

    @Pin(
        name = "testScheduler",
        description = "测试调度服务",
        params = {
            @PinParam(name = "personZid", type = ParamType.STRING, description = "患者ID"),
            @PinParam(name = "type", type = ParamType.STRING, description = "类型"),
            @PinParam(name = "orgZid", type = ParamType.STRING, description = "机构ID")
        }
    )
    public void testScheduler(String parameterBody) {
      System.err.println("测试调度方法... " + parameterBody);
    }
  }
}
