package com.benefitj.system.mapper;

import com.benefitj.scaffold.mapper.SuperMapper;
import com.benefitj.system.model.SysAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.util.Sqls;

import java.util.Date;
import java.util.List;

/**
 * 账号
 */
@Mapper
public interface SysAccountMapper extends SuperMapper<SysAccount> {

  /**
   * 通过用户名查询用户
   */
  default SysAccount findByUserName(@Param("username") String username) {
    return selectOneByExample(example(Sqls.custom().andEqualTo("username", username)));
  }

  /**
   * 统计用户名条数
   *
   * @param username 用户名
   * @return 返回统计的条数
   */
  default int countByUsername(@Param("username") String username) {
    return selectCountByExample(example(Sqls.custom().andEqualTo("username", username)));
  }

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
  List<SysAccount> selectList(@Param("c") SysAccount c,
                              @Param("startTime") Date startTime,
                              @Param("endTime") Date endTime,
                              @Param("multiLevel") boolean multiLevel);

  /**
   * 根据用户ID获取帐号信息
   *
   * @param userId 用户ID
   * @return 返回帐号信息
   */
  default SysAccount selectByUserId(@Param("userId") String userId) {
    return selectOneByExample(example(Sqls.custom().andEqualTo("userId", userId)));
  }


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
    public String selectList(@Param("c") SysAccount c,
                             @Param("startTime") Date startTime,
                             @Param("endTime") Date endTime,
                             @Param("multiLevel") Boolean multiLevel) {
      return new BaseOrgSQL(c.getClass()) {{
        SELECT();
        FROM(getTableName() + " AS t");
        WHERE("t.create_time", startTime, endTime);
        notBlank(c.getUsername(), (username) -> WHERE("t.username LIKE '%" + username + "%'"));
      }}.toString();
    }

  }

}
