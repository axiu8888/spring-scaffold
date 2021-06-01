package com.benefitj.scaffold.quartz;

import com.benefitj.scaffold.quartz.listener.GlobalDeleteJobTaskListener;
import com.benefitj.scaffold.quartz.listener.LoggingSchedulerListener;
import com.benefitj.scaffold.quartz.listener.QuartzStartListener;
import com.benefitj.scaffold.quartz.pin.Pin;
import com.benefitj.scaffold.quartz.pin.PinManager;
import com.benefitj.scaffold.quartz.pin.PinServiceRegistrar;
import com.benefitj.spring.quartz.EnableQuartz;
import com.benefitj.spring.registrar.RegistrarMethodAnnotationBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import tk.mybatis.spring.annotation.MapperScan;

@Import({QuartzController.class, QuartzJobTaskService.class})
@EntityScan({"com.benefitj.scaffold.quartz.entity"}) // quartz entity
@MapperScan({"com.benefitj.scaffold.quartz.mapper"}) // quartz mapper
@EnableQuartz
@Configuration
public class QuartzConfiguration {

  @ConditionalOnMissingBean
  @Bean
  public QuartzStartListener quartzStartListener(SchedulerFactoryBean schedulerFactoryBean,
                                                 QuartzJobTaskService service) {
    return new QuartzStartListener(schedulerFactoryBean, service);
  }

  /**
   * 打印调度任务信息
   */
  @ConditionalOnMissingBean
  @Bean
  public LoggingSchedulerListener loggingGlobalListener() {
    return new LoggingSchedulerListener();
  }

  /**
   * 删除过时的调度任务
   */
  @ConditionalOnMissingBean
  @Bean
  public GlobalDeleteJobTaskListener deleteJobTaskListener() {
    return new GlobalDeleteJobTaskListener();
  }


  @ConditionalOnMissingBean(name = "schedServiceAnnotationProcessor")
  @Bean("schedServiceAnnotationProcessor")
  public RegistrarMethodAnnotationBeanPostProcessor schedServiceAnnotationProcessor(PinServiceRegistrar registrar) {
    return new RegistrarMethodAnnotationBeanPostProcessor(registrar, Pin.class);
  }

  @ConditionalOnMissingBean
  @Bean
  public PinServiceRegistrar pinServiceRegistrar(PinManager pinManager) {
    PinServiceRegistrar registrar = new PinServiceRegistrar();
    registrar.setWorkerPinManager(pinManager);
    return registrar;
  }

  @ConditionalOnMissingBean
  @Bean
  public PinManager pinManager() {
    return PinManager.newManager();
  }

}
