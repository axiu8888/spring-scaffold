package com.benefitj.system.service;

import com.benefitj.system.mapper.SysLogRecordMapper;
import com.benefitj.system.model.SysRequestRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 操作日志
 */
@Service
public class SysLogRecordService extends SysBaseService<SysRequestRecord, SysLogRecordMapper> {

  @Autowired
  private SysLogRecordMapper mapper;

  @Override
  protected SysLogRecordMapper getMapper() {
    return mapper;
  }

  @Override
  public int insert(SysRequestRecord record) {
    return super.insert(record);
  }

}
