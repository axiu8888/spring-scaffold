package com.benefitj.system.service;

import com.benefitj.scaffold.BaseService;
import com.benefitj.system.mapper.SysOrgAndUserMapper;
import com.benefitj.system.model.SysOrgAndUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 机构和用户关联
 */
@Service
public class SysOrgAndUserService extends BaseService<SysOrgAndUser, SysOrgAndUserMapper> {

  @Autowired
  private SysOrgAndUserMapper mapper;

  @Override
  protected SysOrgAndUserMapper getMapper() {
    return mapper;
  }

}
