package com.benefitj.system.service;

import com.benefitj.system.mapper.SysPageElementMapper;
import com.benefitj.system.model.SysPageElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 页面元素
 */
@Service
public class SysPageElementService extends SysBaseService<SysPageElement, SysPageElementMapper> {

  @Autowired
  private SysPageElementMapper mapper;

  @Override
  protected SysPageElementMapper getMapper() {
    return mapper;
  }

}
