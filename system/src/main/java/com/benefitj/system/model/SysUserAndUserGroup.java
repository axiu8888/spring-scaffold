package com.benefitj.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 用户和用户组关联表
 */
@ApiModel("用户信息")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "sys_user_and_usergroup")
public class SysUserAndUserGroup {

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
   * 用户组ID
   */
  @ApiModelProperty("用户组ID")
  @Column(name = "user_group_id", columnDefinition = "varchar(32) comment '用户组ID'", length = 32, nullable = false)
  private String userGroupId;

}
