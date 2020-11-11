package com.benefitj.system.model;

import javax.persistence.*;

/**
 * 机构和角色关联表
 */
@Entity
@Table(name = "sys_org_and_role")
public class SysOrgAndRole {

  /**
   * ID
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /**
   * 机构ID
   */
  @PrimaryKeyJoinColumn
  @Column(name = "org_id", columnDefinition = "varchar(32) comment '机构ID'", length = 32, nullable = false)
  private String orgId;
  /**
   * 角色ID
   */
  @PrimaryKeyJoinColumn
  @Column(name = "role_id", columnDefinition = "varchar(32) comment '角色ID'", length = 32, nullable = false)
  private String roleId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

}
