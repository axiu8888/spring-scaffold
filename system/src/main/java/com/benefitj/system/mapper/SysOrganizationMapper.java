package com.benefitj.system.mapper;

import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.mapper.BaseSQL;
import com.benefitj.scaffold.mapper.SuperMapper;
import com.benefitj.system.model.SysOrg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.Date;
import java.util.List;

/**
 * 机构
 */
@Mapper
public interface SysOrganizationMapper extends SuperMapper<SysOrg> {

  /**
   * 查询满足条件的列表
   *
   * @param condition  分页
   * @param startTime  开始时间
   * @param endTime    结束时间
   * @param multiLevel 是否为多层级机构
   * @return 返回符合的机构
   */
  @SelectProvider(type = Provider.class, method = "selectList")
  List<SysOrg> selectList(@Param("c") SysOrg condition,
                          @Param("startTime") Date startTime,
                          @Param("endTime") Date endTime,
                          @Param("multiLevel") boolean multiLevel);

  /**
   * 使用正则表达式匹配符合的AutoCode
   *
   * @param c         条件
   * @param startTime 开始时间
   * @param endTime   结束时间
   * @param n         至少匹配的次数(0 ~ ∞)
   * @param m         至多匹配的次数
   * @return 返回符合的机构
   */
  @SelectProvider(type = Provider.class, method = "selectByAutoCodeRegex")
  List<SysOrg> selectByAutoCodeRegex(@Param("c") SysOrg c,
                                     @Param("startTime") Date startTime,
                                     @Param("endTime") Date endTime,
                                     @Param("n") int n,
                                     @Param("m") int m);

  /**
   * 通过机构ID查询 autoCode
   *
   * @param id 机构ID
   * @return 返回查询的 autoCode
   */
  @Select("SELECT auto_code FROM sys_organization WHERE id = #{id}")
  String selectAutoCodeById(@Param("id") String id);


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
    public String selectList(@Param("c") SysOrg c,
                             @Param("startTime") Date startTime,
                             @Param("endTime") Date endTime,
                             @Param("multiLevel") boolean multiLevel) {
      return new BaseSQL(c.getClass()) {{
        SELECT();
        FROM(getTableName() + " AS t");
        WHERE("t.create_time", startTime, endTime);
        notNull(c.getActive(), () -> WHERE("t.active = " + c.getActive()));
        notBlank(c.getName(), (name) -> WHERE("t.name LIKE '%" + name + "%'"));
        notBlank(c.getCode(), (code) -> WHERE("t.code LIKE '%" + code + "%'"));
        if (multiLevel) {
          String id = Checker.checkNotBlank(c.getId(), c.getParentId());
          WHERE("(" +
              "t.id = '" + id + "'" +
              " OR t.auto_code LIKE concat(" +
              // 根据autoCode查询
              "(SELECT auto_code FROM " + getTableName() + " WHERE id = '" + id + "' ORDER BY create_time DESC LIMIT 1" +
              "), ':%')" +
              ")"
          );
        } else {
          WHERE("t.parent_id = '" + c.getParentId() + "'");
        }
      }}.toString();
    }


    /**
     * 通过正则表达式查询数据
     *
     * @param c         条件
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param n         至少匹配的次数(0 ~ ∞)
     * @param m         至多匹配的次数
     * @return 返回查询的数据
     */
    public String selectByAutoCodeRegex(@Param("c") SysOrg c,
                                        @Param("startTime") Date startTime,
                                        @Param("endTime") Date endTime,
                                        @Param("n") int n,
                                        @Param("m") int m) {
      // "SELECT * FROM HS_ORG WHERE auto_code REGEXP '^(" + autoCode + "){1}(:[\\\\w-]+){" + n + "," + m + "}$'"
      return new BaseSQL(c.getClass()) {{
        SELECT();
        FROM(getTable().getName() + " AS t");
        WHERE("t.create_time", startTime, endTime);
        notBlank(c.getName(), (name) -> WHERE("t.name LIKE '%#" + name + "%'"));
        notBlank(c.getCode(), (code) -> WHERE("t.code LIKE '%" + code + "%'"));
        WHERE("t.auto_code REGEXP \"^(" + c.getAutoCode() + "){1}(:[\\\\w-]+){" + n + "," + m + "}$\"");
      }}.toString();
    }
  }

}
