package com.benefitj.system.mapper;

import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.mapper.BaseSQL;
import com.benefitj.scaffold.mapper.SuperMapper;
import com.benefitj.system.model.ISysBaseModel;
import com.benefitj.system.model.SysOrganization;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.util.Sqls;

import javax.persistence.Transient;
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
  @SelectProvider(type = BaseOrgProvider.class, method = "selectList")
  List<T> selectList(@Param("c") T c,
                     @Param("startTime") Date startTime,
                     @Param("endTime") Date endTime,
                     @Param("multiLevel") boolean multiLevel);

  /**
   * 获取机构下的菜单列表
   *
   * @param orgId  机构ID
   * @param active 是否可用
   * @return 返回菜单列表
   */
  @Transient
  default List<T> selectPlainList(String orgId, Boolean active) {
    final Sqls sqls = Sqls.custom();
    sqls.andEqualTo("orgId", orgId);
    Checker.checkNotNull(active, () -> sqls.andEqualTo("active", active));
    return selectByExample(example(sqls));
  }


  final class BaseOrgProvider<T extends ISysBaseModel> {

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
      return new BaseSQL(c.getClass()) {{
        if (multiLevel) {
          final EntityTable orgTable = EntityHelper.getEntityTable(SysOrganization.class);
          SELECT("t.*");
          FROM(getTable().getName() + " AS t");
          LEFT_OUTER_JOIN(orgTable.getName() + " AS o ON t.org_id = o.id");
          notNull(startTime, () -> WHERE("create_time >= #{startTime}"));
          notNull(endTime, () -> WHERE("create_time <= #{endTime}"));
          WHERE("(t.org_id = '" + c.getOrgId() + "'" +
              " OR o.auto_code LIKE concat(" +
              "(SELECT auto_code FROM " + orgTable.getName() + " WHERE id = '" + c.getOrgId() + "'), ':%')" +
              ")");
        } else {
          SELECT();
          FROM(getTable().getName() + " AS t");
          WHERE("t.create_time", startTime, endTime);
          WHERE("t.org_id = '" + c.getOrgId() + "'");
        }
        notBlank(c.getName(), (username) -> WHERE("t.name LIKE '%" + username + "%'"));
        notNull(c.getActive(), () -> WHERE("t.active = " + c.getActive()));
      }}.toString();
    }
  }

}
