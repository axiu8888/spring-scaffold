package com.benefitj.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户组
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_user_group")
public class SysUserGroup extends SysBaseModel {

  /**
   * 用户组ID
   */
  @Id
  @Column(name = "id", columnDefinition = "varchar(32) comment '用户组ID'", length = 32)
  private String id;
  /**
   * 用户组编号
   */
  @Column(name = "code", columnDefinition = "varchar(100) comment '用户组编号'", length = 100)
  private String code;

}
