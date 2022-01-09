package com.benefitj.system.mapper;

import com.benefitj.system.model.SysOperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.Date;
import java.util.List;

/**
 * 操作日志
 */
@Mapper
public interface SysOperationLogMapper extends SysBaseMapper<SysOperationLog> {


  /**
   * 查询分页数据
   *
   * @param c          条件
   * @param startTime  开始时间
   * @param endTime    结束时间
   * @param multiLevel 是否查询多层级
   * @return 返回查询的数据
   */
  @SelectProvider(type = Provider.class, method = "selectList")
  @Override
  List<SysOperationLog> selectList(SysOperationLog c, Date startTime, Date endTime, Boolean multiLevel);


  final class Provider {

    /**
     * 查询分页数据
     *
     * @param c          条件
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param multiLevel 是否查询多层级
     * @return 返回查询的数据
     */
    public String selectList(@Param("c") SysOperationLog c,
                             @Param("startTime") Date startTime,
                             @Param("endTime") Date endTime,
                             @Param("multiLevel") boolean multiLevel) {
      return new BaseOrgSQL(c.getClass()) {{
        base(c, startTime, endTime, multiLevel);
        notBlank(c.getModule(), (module) -> WHERE("t.module LIKE '%" + module + "%'"));
        notBlank(c.getOperation(), (operation) -> WHERE("t.operation LIKE '%" + operation + "%'"));
        notBlank(c.getUrl(), (url) -> WHERE("t.url LIKE '%" + url + "%'"));
      }}.toString();
    }

  }

}
