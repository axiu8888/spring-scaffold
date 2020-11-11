package com.benefitj.system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户组
 */
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

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
