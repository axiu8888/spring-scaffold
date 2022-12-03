package com.benefitj.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 机构和角色关联表
 */
@ApiModel("机构和角色关联表")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "sys_org_and_role")
public class SysOrgAndRole {

  /**
   * ID
   */
  @ApiModelProperty("ID")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /**
   * 机构ID
   */
  @ApiModelProperty("机构ID")
  @Column(name = "org_id", columnDefinition = "varchar(32) comment '机构ID'", length = 32, nullable = false)
  private String orgId;
  /**
   * 角色ID
   */
  @ApiModelProperty("角色ID")
  @Column(name = "role_id", columnDefinition = "varchar(32) comment '角色ID'", length = 32, nullable = false)
  private String roleId;

}
