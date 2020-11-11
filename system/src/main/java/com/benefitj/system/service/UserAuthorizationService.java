package com.benefitj.system.service;

import com.benefitj.scaffold.common.LogicException;
import com.benefitj.system.model.SysRole;
import com.benefitj.system.model.SysUser;
import com.benefitj.system.model.SysUserAndRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 用户授权服务
 */
@Service
public class UserAuthorizationService {

  @Autowired
  private SysUserService userService;
  @Autowired
  private SysRoleService roleService;
  @Autowired
  private SysUserAndRoleService uarService;

  /**
   * 关联用户和角色
   *
   * @param userId  用户ID
   * @param roleIds 角色ID
   * @return 返回新增记录的条数
   */
  @Transactional(rollbackFor = Throwable.class)
  public int correlate(String userId, String[] roleIds) {
    SysUser user = userService.get(userId);
    if (user == null) {
      throw new LogicException("无法为此用户添加角色");
    }

    final List<SysUserAndRole> list = new LinkedList<>();
    for (String roleId : roleIds) {
      if (roleService.countByPK(roleId) <= 0) {
        throw new LogicException("角色[" + roleId + "]不存在");
      }
      SysUserAndRole uarCorrelation = new SysUserAndRole(userId, roleId);
      // 统计用户和角色的关联关系是否已存在
      if (uarService.count(uarCorrelation) <= 0) {
        list.add(uarCorrelation);
      }
    }
    return list.isEmpty() ? 0 : uarService.insertAll(list);
  }

  /**
   * 解除用户和角色的关联
   *
   * @param userId  用户ID
   * @param roleIds 角色ID
   * @return 返回解除关联的记录
   */
  @Transactional(rollbackFor = Throwable.class)
  public int uncorrelate(String userId, String[] roleIds) {
    SysUser user = userService.get(userId);
    if (user == null) {
      throw new LogicException("无法解除用户关联的角色");
    }
    return uarService.deleteAll(userId, Arrays.asList(roleIds));
  }

  /**
   * 获取用户拥有的角色
   *
   * @param userId 用户ID
   * @return 返回解除关联的记录
   */
  public List<SysRole> getUserOwnedRoleList(String userId) {
    SysUser user = userService.get(userId);
    return user != null ? uarService.getRoleByUserId(userId) : Collections.emptyList();
  }

}
