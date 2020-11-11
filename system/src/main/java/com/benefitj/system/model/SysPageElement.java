package com.benefitj.system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 页面元素
 */
@Entity
@Table(name = "sys_page_element")
public class SysPageElement extends SysBaseModel {

  /**
   * 父节点
   */
  @PrimaryKeyJoinColumn
  @Column(name = "parent_id", columnDefinition = "varchar(32) comment '父节点'", length = 32)
  private String parentId;
  /**
   * 图标
   */
  @Column(name = "icon", columnDefinition = "varchar(1024) comment '图标'", length = 1024)
  private String icon;
  /**
   * URI
   */
  @Column(name = "uri", columnDefinition = "varchar(1024) comment 'URI'", length = 1024)
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
