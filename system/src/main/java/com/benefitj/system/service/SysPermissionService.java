package com.benefitj.system.service;

import com.benefitj.system.mapper.SysPermissionMapper;
import com.benefitj.system.model.SysPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 权限
 */
@Service
public class SysPermissionService extends SysBaseService<SysPermission, SysPermissionMapper> {

  @Autowired
  private SysPermissionMapper mapper;

  @Override
  protected SysPermissionMapper getMapper() {
    return mapper;
  }

}
