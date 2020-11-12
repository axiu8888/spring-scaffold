package com.benefitj.system.mapper;

import com.benefitj.scaffold.mapper.SuperMapper;
import com.benefitj.system.model.SysRole;
import com.benefitj.system.model.SysUserAndRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Nullable;
import java.beans.Transient;
import java.util.List;

/**
 * 用户和角色关联
 */
@Mapper
public interface SysUserAndRoleMapper extends SuperMapper<SysUserAndRole> {

  /**
   * 查询用户关联的角色
   *
   * @param userId 用户ID
   * @return 返回关联的角色
   */
  @Select("SELECT r.* FROM sys_user_and_role AS uar" +
      " LEFT JOIN sys_role AS r ON r.id = uar.role_id WHERE uar.user_id = #{userId}")
  List<SysRole> findByUser(@Param("userId") String userId);

  /**
   * 统计用户是否拥有角色
   *
   * @param userId 用户ID
   * @param roles  角色ID
   * @return 返回统计的角色的条数
   */
  @Transient
  default int countRoleByUser(String userId, @Nullable List<String> roles) {
    Sqls sqls = Sqls.custom();
    sqls.andEqualTo("userId", userId);
    if (roles != null && !roles.isEmpty()) {
      sqls.andIn("roleId", roles);
    }
    return selectCountByExample(example(sqls));
  }

  /**
   * 统计角色关联的用户
   *
   * @param roles 角色ID
   * @return 返回统计的角色的条数
   */
  @Transient
  default int countUserByRole(List<String> roles) {
    Sqls sqls = Sqls.custom();
    sqls.andIn("roleId", roles);
    return selectCountByExample(example(sqls));
  }

  /**
   * 全出全部的
   *
   * @param userId 用户ID
   * @param roles  角色ID
   * @return 返回删除的条数
   */
  default int deleteAll(String userId, List<String> roles) {
    Sqls sqls = Sqls.custom();
    sqls.andEqualTo("userId", userId);
    sqls.andIn("roleId", roles);
    return deleteByExample(example(sqls));
  }

}
