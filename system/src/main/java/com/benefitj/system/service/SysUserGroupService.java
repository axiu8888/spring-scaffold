package com.benefitj.system.service;

import com.benefitj.system.mapper.SysUserGroupMapper;
import com.benefitj.system.model.SysUserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户组
 */
@Service
public class SysUserGroupService extends SysBaseService<SysUserGroup, SysUserGroupMapper> {

  @Autowired
  private SysUserGroupMapper mapper;

  @Override
  protected SysUserGroupMapper getMapper() {
    return mapper;
  }

}
