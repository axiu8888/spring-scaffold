package com.benefitj.system.model;

import javax.persistence.*;

/**
 * 用户和角色关联表
 */
@Entity
@Table(name = "sys_user_and_role")
public class SysUserAndRole {

  /**
   * ID
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /**
   * 用户ID
   */
  @PrimaryKeyJoinColumn
  @Column(name = "user_id", columnDefinition = "varchar(32) comment '用户ID'", length = 32, nullable = false)
  private String userId;
  /**
   * 角色ID
   */
  @PrimaryKeyJoinColumn
  @Column(name = "role_id", columnDefinition = "varchar(32) comment '角色ID'", length = 32, nullable = false)
  private String roleId;

  public SysUserAndRole() {
  }

  public SysUserAndRole(String userId, String roleId) {
    this.userId = userId;
    this.roleId = roleId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }
}
