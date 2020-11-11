package com.benefitj.system.service;

import com.benefitj.scaffold.common.BaseService;
import com.benefitj.system.mapper.SysRoleAndUserGroupMapper;
import com.benefitj.system.model.SysRoleAndUserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysRoleAndUserGroupService extends BaseService<SysRoleAndUserGroup, SysRoleAndUserGroupMapper> {

  @Autowired
  private SysRoleAndUserGroupMapper mapper;

  @Override
  protected SysRoleAndUserGroupMapper getMapper() {
    return mapper;
  }

}
