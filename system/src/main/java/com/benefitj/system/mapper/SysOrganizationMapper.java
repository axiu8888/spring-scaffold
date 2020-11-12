package com.benefitj.system.mapper;

import com.benefitj.scaffold.mapper.BaseSQL;
import com.benefitj.scaffold.mapper.SuperMapper;
import com.benefitj.system.model.SysOrganization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Nullable;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 机构
 */
@Mapper
public interface SysOrganizationMapper extends SuperMapper<SysOrganization> {

  /**
   * 查询满足条件的列表
   *
   * @param condition  分页
   * @param startTime  开始时间
   * @param endTime    结束时间
   * @param multiLevel 是否为多层级机构
   * @return 返回符合的机构
   */
  @SelectProvider(type = OrgSqlProvider.class, method = "selectList")
  List<SysOrganization> selectList(@Param("c") SysOrganization condition,
                                   @Param("startTime") Date startTime,
                                   @Param("endTime") Date endTime,
                                   @Param("multiLevel") Boolean multiLevel);

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
  @SelectProvider(type = OrgSqlProvider.class, method = "selectByAutoCodeRegex")
  List<SysOrganization> selectByAutoCodeRegex(@Param("c") SysOrganization c,
                                              @Param("startTime") Date startTime,
                                              @Param("endTime") Date endTime,
                                              @Param("n") int n,
                                              @Param("m") int m);

  /**
   * 查询单级子机构
   *
   * @param parentId 父级机构
   * @param active   是否可用
   * @return 返回查询到的子机构
   */
  @Transient
  default List<SysOrganization> selectPlainChildren(String parentId, Boolean active) {
    SysOrganization query = new SysOrganization();
    query.setParentId(parentId);
    query.setActive(active);
    return select(query);
  }

  /**
   * 查询多级机构
   *
   * @param orgId  机构ID
   * @param active 是否可用
   * @param self   是否包含自身
   * @return 返回查询到的多级子机构
   */
  @Transient
  default List<SysOrganization> selectMultiOrgList(String orgId,
                                                   @Nullable Boolean active,
                                                   @Nullable Boolean self) {
    SysOrganization org = selectByPrimaryKey(orgId);
    if (org == null) {
      return Collections.emptyList();
    }
    Sqls sqls = Sqls.custom();
    if (active != null) {
      sqls.andEqualTo("active", active);
    }
    sqls.andLike("autoCode", org.getAutoCode() + ":%");
    List<SysOrganization> children = selectByExample(example(sqls));
    if (!Boolean.TRUE.equals(self) || Boolean.FALSE.equals(active)) {
      return children;
    }
    List<SysOrganization> orgList = new ArrayList<>(children.size() + 1);
    orgList.add(org);
    orgList.addAll(children);
    return orgList;
  }

  /**
   * 通过机构ID查询 autoCode
   *
   * @param id 机构ID
   * @return 返回查询的 autoCode
   */
  @Select("SELECT auto_code FROM sys_organization WHERE id = #{id}")
  String selectAutoCodeById(@Param("id") String id);


  final class OrgSqlProvider {


    /**
     * 查询分页数据
     *
     * @param c          条件
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param multiLevel 是否查询多层级
     * @return 返回查询的数据
     */
    public String selectList(@Param("c") SysOrganization c,
                             @Param("startTime") Date startTime,
                             @Param("endTime") Date endTime,
                             @Param("multiLevel") Boolean multiLevel) {
      return new BaseSQL(c.getClass()) {{
        SELECT();
        FROM(getTable().getName() + " AS t");
        WHERE("t.create_time", startTime, endTime);
        notBlank(c.getName(), (name) -> WHERE("t.name LIKE '%" + name + "%'"));
        notBlank(c.getCode(), (code) -> WHERE("t.code LIKE '%" + code + "%'"));
        if (multiLevel) {
          WHERE("t.auto_code LIKE '" + c.getAutoCode() + ":%'");
        } else {
          WHERE("t.parent_id = '" + c.getParentId() + "'");
        }
        notNull(c.getActive(), () -> WHERE("t.active = " + c.getActive()));
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
    public String selectByAutoCodeRegex(@Param("c") SysOrganization c,
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
