package com.benefitj.system.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * 账号
 */
@Entity
@Table(name = "sys_user")
public class SysUser extends BaseModel implements ISysBaseModel<String> {

  /**
   * ID
   */
  @Id
  @Column(name = "id", columnDefinition = "varchar(32) comment 'ID'", length = 32)
  private String id;
  /**
   * 用户名
   */
  @PrimaryKeyJoinColumn
  @Column(name = "username", columnDefinition = "varchar(100) comment '用户名'", length = 100, nullable = false, unique = true)
  private String username;
  /**
   * 密码
   */
  @JsonIgnore
  @Column(name = "password", columnDefinition = "varchar(200) comment '密码'", length = 200, nullable = false)
  private String password;
  /**
   * 是否被锁住
   */
  @JsonIgnore
  @Column(name = "locked", columnDefinition = "tinyint(1) comment '是否被锁住'")
  private Boolean locked = Boolean.FALSE;
  /**
   * 机构ID
   */
  @Column(name = "org_id", columnDefinition = "varchar(32) comment '机构ID'")
  private String orgId;

  public SysUser() {
  }

  public SysUser(String id) {
    this.id = id;
    this.setActive(null);
  }

  public SysUser(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getOrgId() {
    return orgId;
  }

  public void setLocked(Boolean locked) {
    this.locked = locked;
  }

  @Override
  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  public Boolean getLocked() {
    return locked;
  }

  @JSONField(serialize = false)
  @JsonIgnore
  @Override
  public String getName() {
    return getUsername();
  }

  @JSONField(serialize = false)
  @JsonIgnore
  @Override
  public void setName(String name) {
    this.setUsername(name);
  }
}
