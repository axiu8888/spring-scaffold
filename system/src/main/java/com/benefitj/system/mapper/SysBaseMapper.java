package com.benefitj.system.mapper;

import com.benefitj.scaffold.mapper.SuperMapper;
import com.benefitj.system.model.ISysBaseModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.entity.EntityColumn;

import java.lang.reflect.InvocationTargetException;
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
                     @Param("multiLevel") Boolean multiLevel);

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
                             @Param("multiLevel") Boolean multiLevel) {
      return new BaseOrgSQL(c.getClass()) {{
        base(c, startTime, endTime, multiLevel);
        notBlank(c.getName(), (name) -> WHERE("t.name LIKE '%" + name + "%'"));
        notBlank(c.getCreatorId(), creatorId -> WHERE("t.creator_id = ’" + creatorId + "‘"));
        notNull(c.getActive(), () -> WHERE("t.active = " + c.getActive()));
        getTable().getEntityClassColumns()
            .stream()
            .filter(column -> !column.getColumn().equals("name"))
            .filter(column -> !column.getColumn().equals("creator_id"))
            .filter(column -> !column.getColumn().equals("active"))
            .filter(column -> getValue(column, c) != null)
            .forEach(column -> {
              Object value = getValue(column, c);
              if (value instanceof CharSequence) {
                WHERE("t." + column.getColumn() + " = '" + value + "'");
              } else {
                WHERE("t." + column.getColumn() + " = " + value);
              }
            });
      }}.toString();
    }

    private <T> T getValue(EntityColumn column, Object obj) {
      try {
        return (T) column.getEntityField().getValue(obj);
      } catch (IllegalAccessException | InvocationTargetException e) {
        return null;
      }
    }

  }

}
