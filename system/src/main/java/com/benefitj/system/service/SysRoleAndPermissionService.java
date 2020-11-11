package com.benefitj.system.service;

import com.benefitj.scaffold.common.BaseService;
import com.benefitj.system.mapper.SysRoleAndPermissionMapper;
import com.benefitj.system.model.SysRoleAndPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色和权限关联
 */
@Service
public class SysRoleAndPermissionService extends BaseService<SysRoleAndPermission, SysRoleAndPermissionMapper> {

  @Autowired
  private SysRoleAndPermissionMapper mapper;

  @Override
  protected SysRoleAndPermissionMapper getMapper() {
    return mapper;
  }

}
