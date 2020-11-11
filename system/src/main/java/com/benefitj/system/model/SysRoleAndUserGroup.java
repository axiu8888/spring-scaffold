package com.benefitj.system.model;

import javax.persistence.*;

/**
 * 角色和用户组关联表
 */
@Entity
@Table(name = "sys_role_and_usergroup")
public class SysRoleAndUserGroup {

  /**
   * ID
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /**
   * 角色ID
   */
  @PrimaryKeyJoinColumn
  @Column(name = "role_id", columnDefinition = "varchar(32) comment '角色ID'", length = 32, nullable = false)
  private String roleId;
  /**
   * 用户组ID
   */
  @PrimaryKeyJoinColumn
  @Column(name = "user_group_id", columnDefinition = "varchar(32) comment '用户组ID'", length = 32, nullable = false)
  private String userGroupId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public String getUserGroupId() {
    return userGroupId;
  }

  public void setUserGroupId(String userGroupId) {
    this.userGroupId = userGroupId;
  }

}
