package com.benefitj.system.service;

import com.benefitj.system.mapper.SysOperationLogMapper;
import com.benefitj.system.model.SysOperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 操作日志
 */
@Service
public class SysOperationLogService extends SysBaseService<SysOperationLog, SysOperationLogMapper> {

  @Autowired
  private SysOperationLogMapper mapper;

  @Override
  protected SysOperationLogMapper getMapper() {
    return mapper;
  }

  @Override
  public List<SysOperationLog> getList(SysOperationLog condition, Date startTime, Date endTime, boolean multiLevel) {
    return getMapper().selectList(condition, startTime, endTime, multiLevel);
  }

}
