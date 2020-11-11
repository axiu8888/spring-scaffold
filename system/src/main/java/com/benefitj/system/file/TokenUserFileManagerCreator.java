package com.benefitj.system.file;

import com.benefitj.core.file.IUserFileManager;
import com.benefitj.core.file.UserFileManager;
import com.benefitj.scaffold.security.token.JwtToken;
import com.benefitj.scaffold.security.token.JwtTokenManager;

import java.io.File;

public class TokenUserFileManagerCreator implements UserFileManagerCreator {

  /**
   * 创建新的用户文件管理器
   *
   * @param root
   */
  @Override
  public IUserFileManager create(File root) {
    JwtToken token = JwtTokenManager.currentToken();
    String userId = token.getUserId();
    UserFileManager manager = new UserFileManager();
    manager.setRoot(root, true);
    manager.setUsername(userId);
    return manager;
  }

}
