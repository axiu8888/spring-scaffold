package com.benefitj.system.mapper;

import com.benefitj.scaffold.Checker;
import com.benefitj.system.model.SysRequestRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.util.Sqls;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * 日志信息
 */
@Mapper
public interface SysLogRecordMapper extends SysBaseMapper<SysRequestRecord> {

  /**
   * 查询列表
   *
   * @param c          条件
   * @param startTime  开始时间
   * @param endTime    结束时间
   * @param multiLevel 是否为多层级的机构(不支持)
   * @return 返回列表
   */
  @Override
  @Transient
  default List<SysRequestRecord> selectList(@Param("c") SysRequestRecord c,
                                            @Param("startTime") Date startTime,
                                            @Param("endTime") Date endTime,
                                            @Param("multiLevel") boolean multiLevel) {
    final Sqls sqls = Sqls.custom();
    Checker.checkNotNull(startTime, (value) -> sqls.andGreaterThanOrEqualTo("create_time", value));
    Checker.checkNotNull(endTime, (value) -> sqls.andLessThanOrEqualTo("create_time", value));

    Checker.checkNotBlank(c.getUserId(), (value) -> sqls.andEqualTo("userId", value));
    Checker.checkNotBlank(c.getOrgId(), (value) -> sqls.andEqualTo("orgId", value));
    Checker.checkNotNull(c.getResultCode(), (value) -> sqls.andEqualTo("resultCode", value));
    Checker.checkNotBlank(c.getResultMsg(), (value) -> sqls.andLike("resultMsg", value));
    Checker.checkNotBlank(c.getMethod(), (value) -> sqls.andLike("method", value));
    Checker.checkNotBlank(c.getUrl(), (value) -> sqls.andLike("url", value));
    Checker.checkNotBlank(c.getIpAddr(), (value) -> sqls.andLike("ipAddr", value));
    Checker.checkNotBlank(c.getRemarks(), (value) -> sqls.andLike("remarks", value));
    return selectByExample(example(sqls));
  }


}
