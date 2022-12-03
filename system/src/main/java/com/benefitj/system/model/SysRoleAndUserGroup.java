package com.benefitj.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 角色和用户组关联表
 */
@ApiModel("角色和用户组关联表")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "sys_role_and_usergroup")
public class SysRoleAndUserGroup {

  /**
   * ID
   */
  @ApiModelProperty("ID")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /**
   * 角色ID
   */
  @ApiModelProperty("角色ID")
  @Column(name = "role_id", columnDefinition = "varchar(32) comment '角色ID'", length = 32, nullable = false)
  private String roleId;
  /**
   * 用户组ID
   */
  @ApiModelProperty("用户组ID")
  @Column(name = "user_group_id", columnDefinition = "varchar(32) comment '用户组ID'", length = 32, nullable = false)
  private String userGroupId;

}
