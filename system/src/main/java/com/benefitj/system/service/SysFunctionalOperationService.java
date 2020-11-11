package com.benefitj.system.service;

import com.benefitj.system.mapper.SysFunctionalOperationMapper;
import com.benefitj.system.model.SysFunctionalOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 功能操作
 */
@Service
public class SysFunctionalOperationService extends SysBaseService<SysFunctionalOperation, SysFunctionalOperationMapper> {

  @Autowired
  private SysFunctionalOperationMapper mapper;

  @Override
  protected SysFunctionalOperationMapper getMapper() {
    return mapper;
  }

}
