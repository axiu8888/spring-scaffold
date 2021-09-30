package com.benefitj.system.mapper;

import com.benefitj.system.model.SysDictionaryModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.Date;
import java.util.List;

/**
 * 字典
 */
@Mapper
public interface SysDictionaryMapper extends SysBaseMapper<SysDictionaryModel> {


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
  List<SysDictionaryModel> selectList(SysDictionaryModel c, Date startTime, Date endTime, boolean multiLevel);


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
    public String selectList(@Param("c") SysDictionaryModel c,
                             @Param("startTime") Date startTime,
                             @Param("endTime") Date endTime,
                             @Param("multiLevel") boolean multiLevel) {
      return new BaseOrgSQL(c.getClass()) {{
        base(c, startTime, endTime, multiLevel);
        notBlank(c.getParentId(), (parentId) -> WHERE("t.parent_id = '" + parentId + "'"));
        notBlank(c.getName(), (name) -> WHERE("t.name LIKE '%" + name + "%'"));
        notBlank(c.getCode(), (code) -> WHERE("t.code LIKE '%" + code + "%'"));
        notBlank(c.getAttribute(), (attribute) -> WHERE("t.attribute = '" + attribute + "'"));
        notBlank(c.getValue(), (value) -> WHERE("t.value = '" + value + "'"));
      }}.toString();
    }

  }


}
