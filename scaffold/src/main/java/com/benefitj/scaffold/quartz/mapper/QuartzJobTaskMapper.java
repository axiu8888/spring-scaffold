package com.benefitj.scaffold.quartz.mapper;

import com.benefitj.core.DateFmtter;
import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.mapper.SuperMapper;
import com.benefitj.scaffold.quartz.entity.QuartzJobTaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.util.Sqls;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

/**
 * Cron
 */
@Mapper
public interface QuartzJobTaskMapper extends SuperMapper<QuartzJobTaskEntity> {

  static String fmt(Object time) {
    return DateFmtter.fmt(time, DateFmtter._yMd);
  }

  /**
   * 统计 job name 出现的次数
   *
   * @param jobName job name
   * @return 返回出现的次数
   */
  default int countJobName(@Param("jobName") String jobName) {
    return selectCountByExample(example(Sqls.custom().andEqualTo("jobName", jobName)));
  }

  /**
   * 统计 trigger name 出现的次数
   *
   * @param triggerName trigger name
   * @return 返回出现的次数
   */
  default int countTriggerName(@Param("triggerName") String triggerName) {
    return selectCountByExample(example(Sqls.custom().andEqualTo("triggerName", triggerName)));
  }

  /**
   * 查询调度任务的分页
   *
   * @param condition  条件
   * @param startTime  开始时间
   * @param endTime    结束时间
   * @param multiLevel 是否为多层级机构的调度(不支持)
   * @return 返回查询的列表
   */
  @Transient
  default List<QuartzJobTaskEntity> selectList(QuartzJobTaskEntity condition, Date startTime, Date endTime, Boolean multiLevel) {
    final Sqls sqls = Sqls.custom();
    Checker.checkNotNull(startTime, () -> sqls.andGreaterThanOrEqualTo("create_time", fmt(startTime)));
    Checker.checkNotNull(endTime, () -> sqls.andLessThanOrEqualTo("create_time", fmt(endTime)));
    Checker.checkNotNull(condition.getTriggerType(), triggerType -> sqls.andLike("triggerType", triggerType.name()));
    Checker.checkNotBlank(condition.getJobGroup(), jobGroup -> sqls.andLike("jobGroup", jobGroup));
    Checker.checkNotBlank(condition.getJobName(), jobName -> sqls.andLike("jobName", jobName));
    Checker.checkNotBlank(condition.getTriggerGroup(), triggerGroup -> sqls.andLike("triggerGroup", triggerGroup));
    Checker.checkNotBlank(condition.getTriggerName(), triggerName -> sqls.andLike("triggerName", triggerName));
    Checker.checkNotBlank(condition.getOrgId(), orgId -> sqls.andEqualTo("orgId", orgId));
    Checker.checkNotBlank(condition.getOwner(), owner -> sqls.andEqualTo("owner", owner));
    Checker.checkNotBlank(condition.getOwnerType(), ownerType -> sqls.andEqualTo("ownerType", ownerType));
    Checker.checkNotNull(condition.getActive(), active -> sqls.andEqualTo("active", active));
    return selectByExample(example(sqls));
  }

}
