package com.benefitj.system.mapper;

import com.benefitj.system.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.Date;
import java.util.List;

/**
 * 用户信息
 */
@Mapper
public interface SysUserMapper extends SysBaseMapper<SysUser> {

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
  List<SysUser> selectList(SysUser c, Date startTime, Date endTime, boolean multiLevel);


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
    public String selectList(@Param("c") SysUser c,
                             @Param("startTime") Date startTime,
                             @Param("endTime") Date endTime,
                             @Param("multiLevel") boolean multiLevel) {
      return new BaseOrgSQL(c.getClass()) {{
        base(c, startTime, endTime, multiLevel);
        notBlank(c.getName(), (first_name) -> WHERE("t.first_name LIKE '%" + first_name + "%'"));
      }}.toString();
    }

  }

}
