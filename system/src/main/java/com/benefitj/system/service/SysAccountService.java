package com.benefitj.system.service;

import com.benefitj.core.HexUtils;
import com.benefitj.core.IdUtils;
import com.benefitj.scaffold.BaseService;
import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.LogicException;
import com.benefitj.system.mapper.SysAccountMapper;
import com.benefitj.system.model.SysAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class SysAccountService extends BaseService<SysAccount, SysAccountMapper> {

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private SysAccountMapper mapper;
  @Autowired
  private CacheService cacheService;

  @Override
  protected SysAccountMapper getMapper() {
    return mapper;
  }

  /**
   * 通过账号名获取
   *
   * @param username 用户名
   * @return 返回查询的账号
   */
  public SysAccount getByUsername(String username) {
    SysAccount account = cacheService.getAccount(username);
    if (account != null) {
      return account;
    }
    account = getMapper().findByUserName(username);
    if (account != null) {
      // 缓存
      cacheService.setAccount(account);
    }
    return account;
  }

  public int countByUsername(String username) {
    return getMapper().countByUsername(username);
  }

  /**
   * 获取账号
   *
   * @param id 账号ID
   * @return 返回查询到的账号
   */
  public SysAccount get(String id) {
    return getByPK(id);
  }

  /**
   * 创建账号
   *
   * @param account 账号
   */
  public SysAccount save(SysAccount account) {
    if (countByUsername(account.getUsername()) > 0) {
      throw new LogicException("此账号已存在");
    }

    account.setId(IdUtils.uuid());
    // 对密码加密
    String hex = HexUtils.bytesToHex(account.getPassword().getBytes());
    account.setPassword(passwordEncoder.encode(hex));
    account.setCreateTime(new Date());
    account.setActive(Boolean.TRUE);
    super.insert(account);

    // 缓存
    cacheService.setAccount(account);

    return account;
  }

  /**
   * 改变账号可用状态
   *
   * @param id     账号ID
   * @param active 状态
   * @return 返回是否更新
   */
  public boolean changeActive(String id, Boolean active) {
    SysAccount account = get(id);
    if (account != null) {
      account.setActive(Checker.checkNotNull(active, account.getActive()));
      account.setUpdateTime(new Date());
      if (Boolean.FALSE.equals(active)) {
        cacheService.deleteAccount(account.getUsername());
      }
      return updateByPKSelective(account) > 0;
    }
    return false;
  }

  /**
   * 获取机构的账号
   *
   * @param condition  条件
   * @param multiLevel 多层级(当前账号机构下的所有子级机构)
   * @return 返回账号列表
   */
  public List<SysAccount> getList(SysAccount condition, Boolean multiLevel) {
    return getMapper().selectList(condition, null, null, multiLevel);
  }

  /**
   * 根据用户ID获取帐号信息
   *
   * @param userId 用户ID
   * @return 返回帐号信息
   */
  public SysAccount getByUserId(String userId) {
    SysAccount account = cacheService.getAccountByUserId(userId);
    if (account != null) {
      return account;
    }
    account = getMapper().selectByUserId(userId);
    if (account != null) {
      cacheService.setUsername(userId, account.getUsername());
      cacheService.setAccount(account);
    }
    return account;
  }

  /**
   * 修改密码
   *
   * @param userId      用户ID
   * @param oldPassword 旧密码
   * @param newPassword 新密码
   * @return 返回是否修改
   */
  public boolean changePassword(String userId, String oldPassword, String newPassword) {
    SysAccount account = getByUserId(userId);
    if (account != null) {
      try {
        // 验证账号和密码
        validate(account, oldPassword);
      } catch (LogicException e) {
        throw new LogicException("旧密码错误");
      }
      // 对密码加密
      String hex = HexUtils.bytesToHex(newPassword.getBytes(StandardCharsets.UTF_8));
      account.setPassword(passwordEncoder.encode(hex));
      account.setUpdateTime(new Date());
      cacheService.deleteAccount(account.getUsername());
      return updateByPK(account) > 0;
    }
    return false;
  }

  public void validate(SysAccount account, String password) {
    String rawPassword = HexUtils.bytesToHex(password.getBytes(StandardCharsets.UTF_8));
    if (account == null || !passwordEncoder.matches(rawPassword, account.getPassword())) {
      throw new LogicException("账号或密码错误");
    }

    if (!Boolean.TRUE.equals(account.getActive())) {
      throw new LogicException("账号不可用");
    }

    if (Boolean.TRUE.equals(account.getLocked())) {
      throw new LogicException("账号被锁定");
    }
  }

}
