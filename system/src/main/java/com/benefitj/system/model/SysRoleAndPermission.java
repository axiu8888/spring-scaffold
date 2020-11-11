package com.benefitj.system.model;

import javax.persistence.*;

/**
 * 角色和权限关联表
 */
@Entity
@Table(name = "sys_role_and_permission")
public class SysRoleAndPermission {

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
   * 权限ID
   */
  @PrimaryKeyJoinColumn
  @Column(name = "permission_id", columnDefinition = "varchar(32) comment '权限ID'", length = 32, nullable = false)
  private String permissionId;

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

  public String getPermissionId() {
    return permissionId;
  }

  public void setPermissionId(String permissionId) {
    this.permissionId = permissionId;
  }

}
