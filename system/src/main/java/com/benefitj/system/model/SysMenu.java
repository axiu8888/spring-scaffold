package com.benefitj.system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 系统菜单
 */
@Entity
@Table(name = "sys_menu")
public class SysMenu extends SysBaseModel {
  /**
   * 父节点
   */
  @PrimaryKeyJoinColumn
  @Column(name = "parent_id", columnDefinition = "varchar(32) comment '父节点'", length = 32)
  private String parentId;
  /**
   * 菜单图标
   */
  @Column(name = "icon", columnDefinition = "varchar(1024) comment '菜单图标'", length = 1024)
  private String icon;
  /**
   * 地址
   */
  @Column(name = "uri", columnDefinition = "varchar(1024) comment '地址'", length = 1024)
  private String uri;

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }
}
