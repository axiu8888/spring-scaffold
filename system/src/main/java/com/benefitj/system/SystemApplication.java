package com.benefitj.system;

import com.alibaba.fastjson.JSON;
import com.benefitj.core.DateFmtter;
import com.benefitj.core.EventLoop;
import com.benefitj.quartz.entity.QrtzJobTask;
import com.benefitj.quartz.job.JobWorker;
import com.benefitj.quartz.service.QrtzJobTaskService;
import com.benefitj.scaffold.SwaggerConfig;
import com.benefitj.scaffold.security.token.JwtProperty;
import com.benefitj.spring.applicationevent.ApplicationEventListener;
import com.benefitj.spring.athenapdf.EnableAthenapdfConfiguration;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;

import java.util.concurrent.TimeUnit;

/**
 * 系统管理
 */
@EnableAthenapdfConfiguration
@SpringBootApplication
public class SystemApplication {
  public static void main(String[] args) {
    SpringApplication.run(SystemApplication.class, args);
  }


  /**
   * 接口文档配置
   */
  @Configuration
  public static class ApiConfig extends SwaggerConfig {
    /**
     * 端口号
     */
    @Value("#{ environment['server.port'] ?: '8080'}")
    private String port;
    /**
     * 上下文路径
     */
    @Value("#{ environment['server.servlet.context-path'] ?: ''}")
    private String contextPath;

    @Override
    public String basePackage() {
      return "com.benefitj";
    }

    @Override
    public ApiInfo apiInfo() {
      return new ApiInfoBuilder()
          .title("用户模块的API")
          //.setResourceGroupingStrategy(SpringSwaggerConfig.defaultResourceGroupingStrategy())
          .description("用户注册，登录认证，管理权限等方面的操作！")
          .termsOfServiceUrl(String.format("http://127.0.0.1:%s/%s", port, contextPath))
          .contact(new Contact("DING XIU AN", "", "dingxiuan@163.com"))
          .version("1.0.0")
          .license("The Apache License, Version 2.0")
          .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
          .build();
    }
  }

  @Component
  public static class JwtPrinter {

    @ApplicationEventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) throws Exception {
      EventLoop.multi().schedule(() -> {
        ApplicationContext ctx = event.getApplicationContext();
        JwtProperty jwtProperty = ctx.getBean(JwtProperty.class);
        System.err.println("\n----- SIGNING_KEY -----------------------");
        System.err.println(jwtProperty.getSigningKey());
        System.err.println("----- SIGNING_KEY -----------------------\n");
      }, 3, TimeUnit.SECONDS);
    }
  }

  /**
   * 测试 Quartz 的 JobWorker
   */
  @Component("jobTaskWorker")
  public static class JobTaskWorker implements JobWorker {

    private static final Logger logger = LoggerFactory.getLogger(JobTaskWorker.class);

    @Override
    public void execute(JobExecutionContext context, JobDetail detail, String taskId) throws JobExecutionException {
      try {
        JobDataMap jobDataMap = detail.getJobDataMap();
        QrtzJobTaskService taskService = getBean(QrtzJobTaskService.class);
        QrtzJobTask task = taskService.get(taskId);
        System.err.println("\n-------------------------------------");
        System.err.println("task: " + JSON.toJSONString(task));
        System.err.println("now: " + DateFmtter.fmtNowS());
        System.err.println("key: " + detail.getKey());
        System.err.println("jobClass: " + detail.getJobClass());
        System.err.println("jobDataMap: " + JSON.toJSONString(jobDataMap));
        System.err.println("description: " + detail.getDescription());
        System.err.println("refireCount: " + context.getRefireCount());
        System.err.println("nextFireTime: " + fmtS(context.getNextFireTime()));
        System.err.println("thread: " + Thread.currentThread().getName());
        System.err.println("-------------------------------------\n");
      } catch (Exception e) {
        logger.error("throws: " + e.getMessage(), e);
        throw new JobExecutionException(e);
      }
    }
  }
}