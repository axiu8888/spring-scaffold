package com.benefitj.system.mapper;

import com.benefitj.scaffold.common.mapper.BaseSQL;
import com.benefitj.system.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.Date;
import java.util.List;

/**
 * 账号
 */
@Mapper
public interface SysUserMapper extends SysBaseMapper<SysUser> {

  /**
   * 通过用户名查询用户
   */
  @Select("SELECT * FROM sys_user WHERE username = #{username}")
  SysUser findByUserName(@Param("username") String username);

  /**
   * 统计用户名条数
   *
   * @param username 用户名
   * @return 返回统计的条数
   */
  @Select("SELECT count(u.id) FROM sys_user u WHERE username = #{username}")
  int countByUsername(@Param("username") String username);

  /**
   * 查询分页数据
   *
   * @param c          条件
   * @param startTime  开始时间
   * @param endTime    结束时间
   * @param multiLevel 是否查询多层级
   * @return 返回查询的数据
   */
  @Override
  @SelectProvider(type = UserProvider.class, method = "selectList")
  List<SysUser> selectList(@Param("c") SysUser c,
                           @Param("startTime") Date startTime,
                           @Param("endTime") Date endTime,
                           @Param("multiLevel") boolean multiLevel);


  final class UserProvider {

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
      return new BaseSQL(c.getClass()) {{
        if (multiLevel) {
          SELECT("u.*");
          FROM("sys_user AS u");
          LEFT_OUTER_JOIN("sys_organization AS o ON u.org_id = o.id");
          notNull(startTime, () -> WHERE("create_time >= #{startTime}"));
          notNull(endTime, () -> WHERE("create_time <= #{endTime}"));
          WHERE("(u.org_id = '" + c.getOrgId() + "'" +
              " OR o.auto_code LIKE concat(" +
              "(SELECT auto_code FROM sys_organization WHERE id = '" + c.getOrgId() + "'), ':%')" +
              ")");
        } else {
          SELECT();
          FROM(getTable().getName() +" AS u");
          WHERE("u.create_time", startTime, endTime);
          WHERE("u.org_id = '" + c.getOrgId() + "'");
        }
        notBlank(c.getUsername(), (username) -> WHERE("u.username LIKE '%" + username + "%'"));
        notNull(c.getActive(), () -> WHERE("u.active = " + c.getActive()));
      }}.toString();
    }

  }

}
