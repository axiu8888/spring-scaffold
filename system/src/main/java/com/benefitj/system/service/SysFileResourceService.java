package com.benefitj.system.service;

import com.benefitj.system.mapper.SysFileResourceMapper;
import com.benefitj.system.model.SysFileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysFileResourceService extends SysBaseService<SysFileResource, SysFileResourceMapper> {

  @Autowired
  private SysFileResourceMapper mapper;

  @Override
  protected SysFileResourceMapper getMapper() {
    return mapper;
  }

}
