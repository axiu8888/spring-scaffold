package com.benefitj.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 用户和角色关联表
 */
@ApiModel("用户信息")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "sys_user_and_role")
public class SysUserAndRole {

  /**
   * ID
   */
  @ApiModelProperty("ID")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /**
   * 用户ID
   */
  @ApiModelProperty("用户ID")
  @Column(name = "user_id", columnDefinition = "varchar(32) comment '用户ID'", length = 32, nullable = false)
  private String userId;
  /**
   * 角色ID
   */
  @ApiModelProperty("角色ID")
  @Column(name = "role_id", columnDefinition = "varchar(32) comment '角色ID'", length = 32, nullable = false)
  private String roleId;

}
