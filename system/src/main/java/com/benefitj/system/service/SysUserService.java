package com.benefitj.system.service;

import com.benefitj.core.IdUtils;
import com.benefitj.system.mapper.SysUserMapper;
import com.benefitj.system.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户信息
 */
@Service
public class SysUserService extends SysBaseService<SysUser, SysUserMapper> {

  @Autowired
  private SysUserMapper mapper;

  @Override
  protected SysUserMapper getMapper() {
    return mapper;
  }

  /**
   * 获取用户信息
   *
   * @param id 用户ID
   * @return 返回用户信息
   */
  public SysUser get(String id) {
    return getMapper().selectByPrimaryKey(id);
  }

  /**
   * 保存用户
   *
   * @param user 用户
   * @return 返回保存的信息
   */
  public SysUser save(SysUser user) {
    if (StringUtils.isNotBlank(user.getId())) {
      user.setUpdateTime(new Date());
      updateByPK(user);
    } else {
      user.setId(IdUtils.uuid());
      user.setCreateTime(new Date());
      user.setActive(Boolean.TRUE);
      insert(user);
    }
    return user;
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
      user.setActive(active);
      return updateByPKSelective(user) > 0;
    }
    return false;
  }
}
