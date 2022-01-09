package com.benefitj.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 角色和权限关联表
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
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
  @Column(name = "role_id", columnDefinition = "varchar(32) comment '角色ID'", length = 32, nullable = false)
  private String roleId;
  /**
   * 权限ID
   */
  @Column(name = "permission_id", columnDefinition = "varchar(32) comment '权限ID'", length = 32, nullable = false)
  private String permissionId;

}
