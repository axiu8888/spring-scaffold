package com.benefitj.system.file;

import com.benefitj.core.file.IUserFileManager;

import java.io.File;

/**
 * 用户文件管理器
 */
public interface UserFileManagerCreator {

  /**
   * 创建新的用户文件管理器
   */
  IUserFileManager create(File root);

}
