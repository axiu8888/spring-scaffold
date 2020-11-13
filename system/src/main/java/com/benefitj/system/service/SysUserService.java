package com.benefitj.system.service;

import com.benefitj.core.HexUtils;
import com.benefitj.core.IdUtils;
import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.LogicException;
import com.benefitj.scaffold.security.exception.PermissionException;
import com.benefitj.system.mapper.SysUserMapper;
import com.benefitj.system.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SysUserService extends SysBaseService<SysUser, SysUserMapper> {

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private SysUserMapper mapper;

  @Override
  protected SysUserMapper getMapper() {
    return mapper;
  }

  /**
   * 通过用户名获取
   *
   * @param username 用户名
   * @return 返回查询的用户
   */
  public SysUser getByUsername(String username) {
    SysUser user = getMapper().findByUserName(username);
    checkUserPermission(user);
    return user;
  }

  public int countByUsername(String username) {
    return getMapper().countByUsername(username);
  }

  /**
   * 检查用户名
   *
   * @param username 用户名
   * @throws IllegalStateException
   */
  public void checkUsernameExist(String username) throws IllegalStateException {
    if (countByUsername(username) > 0) {
      throw new LogicException("此用户已存在");
    }
  }

  /**
   * 检查机构层级
   *
   * @param userId 用户ID
   * @return 返回是否可以继续操作
   */
  public boolean checkOrgLevelByUserId(String userId) {
    if (currentToken() != null) {
      String orgId = getOrgByUserId(userId);
      return StringUtils.isNotBlank(orgId) && checkOrgLevel(orgId);
    }
    return true;
  }

  /**
   * 检查用户权限
   *
   * @param user 用户
   * @throws PermissionException
   */
  public void checkUserPermission(SysUser user) throws PermissionException {
    if (!checkPermission(user)) {
      logger.info("当前用户无权操作此机构的用户, checkId: {}, standardId: {}", user.getOrgId(), currentOrgId());
      throw new PermissionException("当前用户无权操作此用户");
    }
  }

  /**
   * 获取用户
   *
   * @param id 用户ID
   * @return 返回查询到的用户
   */
  public SysUser get(String id) {
    return checkOrgLevelByUserId(id) ? getByPK(id) : null;
  }

  /**
   * 创建用户
   *
   * @param user 用户信息
   */
  public SysUser create(SysUser user) {
    checkUsernameExist(user.getUsername());
    checkUserPermission(user);

    user.setOrgId(Checker.checkNotBlank(user.getOrgId(), currentOrgId()));
    if (StringUtils.isBlank(user.getOrgId())) {
      throw new LogicException("用户没有机构");
    }
    user.setId(IdUtils.uuid());
    // 对密码加密
    String hex = HexUtils.bytesToHex(user.getPassword().getBytes());
    user.setPassword(passwordEncoder.encode(hex));
    user.setCreateTime(new Date());
    user.setCreatorId(currentUserId());
    user.setActive(Boolean.TRUE);
    super.insert(user);
    return user;
  }

  /**
   * 更新用户
   *
   * @param user 用户
   * @return 返回更新的数据
   */
  public SysUser update(SysUser user) {
    SysUser existUser = getByPK(user.getId());
    if (existUser == null) {
      throw new LogicException("用户不存在");
    }
    // 检查用户名
    String newUsername = user.getUsername();
    if (!newUsername.equals(existUser.getUsername())) {
      checkUsernameExist(newUsername);
    }
    checkUserPermission(user);
    existUser.setOrgId(user.getOrgId());
    existUser.setUsername(newUsername);
    existUser.setUpdateTime(new Date());
    super.updateByPKSelective(existUser);
    return existUser;
  }

  /**
   * 删除用户
   *
   * @param id    用户ID
   * @param force 是否强制删除
   * @return 返回删除条数，如果被删除成功，应该返回 1, 否则返回 0
   */
  public int delete(String id, boolean force) {
    SysUser user = getByPK(id);
    if (user != null) {
      checkUserPermission(user);
      return super.deleteByPK(user.getId());
    }
    return 0;
  }

  /**
   * 改变用户可用状态
   *
   * @param id     用户ID
   * @param active 状态
   * @return 返回是否更新
   */
  public boolean changeActive(String id, Boolean active) {
    SysUser user = get(id);
    if (user != null) {
      user.setActive(Checker.checkNotNull(active, user.getActive()));
      user.setUpdateTime(new Date());
      return updateByPKSelective(user) > 0;
    }
    return false;
  }

  /**
   * 获取机构的用户
   *
   * @param orgId      机构ID
   * @param active     是否可用
   * @param multiLevel 多层级(当前用户机构下的所有子级机构)
   * @return 返回用户列表
   */
  public List<SysUser> getUserList(String orgId, Boolean active, Boolean multiLevel) {
    if (Boolean.TRUE.equals(multiLevel)) {
      SysUser user = new SysUser();
      user.setOrgId(orgId);
      user.setActive(active);
      return getMapper().selectList(user, null, null, true);
    }
    return getMapper().selectPlainList(orgId, active);
  }

  /**
   * 通过用户的ID查询机构ID
   *
   * @param userId 用户ID
   * @return 返回机构ID
   */
  public String getOrgByUserId(String userId) {
    SysUser user = getMapper().selectByPrimaryKey(userId);
    return user != null ? user.getOrgId() : null;
  }
}
