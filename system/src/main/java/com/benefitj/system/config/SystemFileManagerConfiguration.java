package com.benefitj.system.config;

import com.benefitj.system.file.FileManagerFilter;
import com.benefitj.system.file.SimpleUserFileManagerFactory;
import com.benefitj.system.file.SystemFileManager;
import com.benefitj.system.file.UserFileManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.File;

/**
 * 系统文件
 */
@Configuration
public class SystemFileManagerConfiguration {

  @Value("#{ @environment['com.benefitj.file-manager.root'] ?: '/systemRoot/' }")
  private String root;

  /**
   * 系统文件管理
   */
  @ConditionalOnMissingBean(SystemFileManager.class)
  @Bean
  public SystemFileManager systemFileManager(UserFileManagerFactory userFileManagerCreator) {
    return new SystemFileManager(new File(root), userFileManagerCreator);
  }

  /**
   * 用户文件管理创建对象
   */
  @ConditionalOnMissingBean(SimpleUserFileManagerFactory.class)
  @Bean
  public SimpleUserFileManagerFactory userFileManagerFactory() {
    return new SimpleUserFileManagerFactory();
  }

  /**
   * 文件管理的过滤器
   */
  @Order(100)
  @Bean
  public FileManagerFilter fileManagerFilter(SystemFileManager fileManager) {
    return new FileManagerFilter(fileManager);
  }

}
