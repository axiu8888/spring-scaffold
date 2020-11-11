package com.benefitj.system.file;

import com.benefitj.core.file.FileManager;
import com.benefitj.core.file.IUserFileManager;
import com.benefitj.core.local.LocalCache;
import com.benefitj.core.local.LocalCacheFactory;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 系统文件管理
 */
public class SystemFileManager extends FileManager {

  /**
   * 本地用户文件缓存
   */
  private final LocalCache<IUserFileManager> userFileManagerCache = LocalCacheFactory.newCache(this::newUserFileManager);
  /**
   * 用户文件缓存
   */
  private final Map<String, IUserFileManager> userFileManagerMap = new WeakHashMap<>();

  /**
   * UserFileManager创建器
   */
  private UserFileManagerCreator managerCreator;

  public SystemFileManager(UserFileManagerCreator managerCreator) {
    this.managerCreator = managerCreator;
  }

  public SystemFileManager(File root, UserFileManagerCreator managerCreator) {
    super(root);
    this.managerCreator = managerCreator;
  }

  public UserFileManagerCreator getManagerCreator() {
    return managerCreator;
  }

  public void setManagerCreator(UserFileManagerCreator managerCreator) {
    this.managerCreator = managerCreator;
  }

  protected IUserFileManager newUserFileManager() {
    return getManagerCreator().create(getRoot());
  }


  public LocalCache<IUserFileManager> getUserFileManagerCache() {
    return userFileManagerCache;
  }

  public Map<String, IUserFileManager> getUserFileManagerMap() {
    return userFileManagerMap;
  }

  public IUserFileManager getUserFileManager(String key) {
    return getUserFileManagerMap().get(key);
  }

}
