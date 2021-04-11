package com.benefitj.system.mapper;

import com.benefitj.scaffold.mapper.SuperMapper;
import com.benefitj.system.model.ISysBaseModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.Date;
import java.util.List;

@RegisterMapper
public interface SysBaseMapper<T extends ISysBaseModel> extends SuperMapper<T> {

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
  List<T> selectList(@Param("c") T c,
                     @Param("startTime") Date startTime,
                     @Param("endTime") Date endTime,
                     @Param("multiLevel") boolean multiLevel);

  final class Provider<T extends ISysBaseModel> {

    /**
     * 查询分页数据
     *
     * @param c          条件
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param multiLevel 是否查询多层级
     * @return 返回查询的数据
     */
    public String selectList(@Param("c") T c,
                             @Param("startTime") Date startTime,
                             @Param("endTime") Date endTime,
                             @Param("multiLevel") boolean multiLevel) {
      return new BaseOrgSQL(c.getClass()) {{
        base(c, startTime, endTime, multiLevel);
        notBlank(c.getName(), (name) -> WHERE("t.name LIKE '%" + name + "%'"));
      }}.toString();
    }
  }

}
