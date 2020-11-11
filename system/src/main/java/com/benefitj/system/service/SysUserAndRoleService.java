package com.benefitj.system.service;

import com.benefitj.scaffold.common.BaseService;
import com.benefitj.system.mapper.SysUserAndRoleMapper;
import com.benefitj.system.model.SysRole;
import com.benefitj.system.model.SysUserAndRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 用户和角色关联
 */
@Service
public class SysUserAndRoleService extends BaseService<SysUserAndRole, SysUserAndRoleMapper> {

  @Autowired
  private SysUserAndRoleMapper mapper;

  @Override
  protected SysUserAndRoleMapper getMapper() {
    return mapper;
  }

  @Override
  public int insert(SysUserAndRole record) {
    return super.insert(record);
  }

  @Override
  public int insertAll(Collection<SysUserAndRole> records) {
    return super.insertAll(records);
  }

  @Override
  public int delete(SysUserAndRole record) {
    return super.delete(record);
  }

  @Override
  public int deleteByPK(String pk) {
    return super.deleteByPK(pk);
  }

  /**
   * 统计用户是否拥有角色
   *
   * @param userId 用户ID
   * @param roles  角色ID
   * @return 返回统计的角色的条数
   */
  public int countRoleByUser(String userId, String... roles) {
    return getMapper().countRoleByUser(userId, Arrays.asList(roles));
  }

  /**
   * 统计角色关联的用户
   *
   * @param roles 角色ID
   * @return 返回统计的角色的条数
   */
  public int countUserByRoles(List<String> roles) {
    return getMapper().countUserByRole(roles);
  }

  /**
   * 通过用户获取关联的角色
   *
   * @param userId 用户ID
   * @return 返回用户拥有的角色
   */
  public List<SysRole> getRoleByUserId(String userId) {
    return getMapper().findByUser(userId);
  }

  /**
   * 删除用户关联的全部角色
   *
   * @param userId 用户ID
   * @param roles  角色ID
   * @return 返回删除的条数
   */
  public int deleteAll(String userId, List<String> roles) {
    return getMapper().deleteAll(userId, roles);
  }

}
