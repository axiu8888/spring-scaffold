package com.benefitj.system.service;

import com.benefitj.scaffold.common.BaseService;
import com.benefitj.system.mapper.SysUserAndUserGroupMapper;
import com.benefitj.system.model.SysUserAndUserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户和用户组关联
 */
@Service
public class SysUserAndUserGroupService extends BaseService<SysUserAndUserGroup, SysUserAndUserGroupMapper> {

  @Autowired
  private SysUserAndUserGroupMapper mapper;

  @Override
  protected SysUserAndUserGroupMapper getMapper() {
    return mapper;
  }

}
