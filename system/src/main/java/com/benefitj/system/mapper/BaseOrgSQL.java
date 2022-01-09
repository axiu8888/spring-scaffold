package com.benefitj.system.mapper;

import com.benefitj.scaffold.mapper.BaseSQL;
import com.benefitj.system.model.ISysBaseModel;
import com.benefitj.system.model.SysOrganization;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.util.Date;

public class BaseOrgSQL extends BaseSQL {

  public BaseOrgSQL(Class<?> entityType) {
    super(entityType);
  }

  public BaseOrgSQL(EntityTable table) {
    super(table);
  }

  public BaseOrgSQL base(@Param("c") ISysBaseModel<?> c,
                         @Param("startTime") Date startTime,
                         @Param("endTime") Date endTime,
                         @Param("multiLevel") Boolean multiLevel) {
    if (Boolean.TRUE.equals(multiLevel)) {
      final EntityTable orgTable = EntityHelper.getEntityTable(SysOrganization.class);
      SELECT("t.*");
      FROM(getTableName() + " AS t");
      LEFT_OUTER_JOIN(orgTable.getName() + " AS o ON t.org_id = o.id");
      WHERE("t.create_time", startTime, endTime);
      WHERE("(" +
          "t.org_id = '" + c.getOrgId() + "'" +
          " OR o.auto_code LIKE concat(" +
          // 根据autoCode查询
          "(SELECT auto_code FROM " + orgTable.getName() + " WHERE id = '" + c.getOrgId() + "' ORDER BY create_time DESC LIMIT 1" +
          "), ':%')" +
          ")"
      );
    } else {
      SELECT();
      FROM(getTableName() + " AS t");
      WHERE("t.create_time", startTime, endTime);
      WHERE("t.org_id = '" + c.getOrgId() + "'");
    }
    notNull(c.getActive(), () -> WHERE("t.active = " + c.getActive()));
    return this;
  }

}
