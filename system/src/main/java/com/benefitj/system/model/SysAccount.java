package com.benefitj.system.model;

import javax.persistence.*;

/**
 * 账号
 */
@Entity
@Table(name = "sys_account")
public class SysAccount extends BaseModel {

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
  @Column(name = "password", columnDefinition = "varchar(200) comment '密码'", length = 200, nullable = false)
  private String password;
  /**
   * 是否被锁住
   */
  @Column(name = "locked", columnDefinition = "tinyint(1) comment '是否被锁住'")
  private Boolean locked = Boolean.FALSE;
  /**
   * 账号ID
   */
  @Column(name = "user_id", columnDefinition = "varchar(32) comment '用户ID'", nullable = false)
  private String userId;

  public SysAccount() {
  }

  public SysAccount(String id) {
    this.id = id;
    this.setActive(null);
  }

  public SysAccount(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getId() {
    return id;
  }

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

  public void setLocked(Boolean locked) {
    this.locked = locked;
  }

  public Boolean getLocked() {
    return locked;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
