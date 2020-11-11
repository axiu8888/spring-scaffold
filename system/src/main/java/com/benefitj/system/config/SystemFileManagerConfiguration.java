package com.benefitj.system.config;

import com.benefitj.system.file.SystemFileManager;
import com.benefitj.system.file.TokenUserFileManagerCreator;
import com.benefitj.system.file.UserFileManagerCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
  public SystemFileManager systemFileManager(UserFileManagerCreator userFileManagerCreator) {
    return new SystemFileManager(new File(root), userFileManagerCreator);
  }

  /**
   * 用户文件管理创建对象
   */
  @ConditionalOnMissingBean(TokenUserFileManagerCreator.class)
  @Bean
  public TokenUserFileManagerCreator userFileManagerCreator() {
    return new TokenUserFileManagerCreator();
  }

}
