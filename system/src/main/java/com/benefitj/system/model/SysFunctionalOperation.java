package com.benefitj.system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 功能性操作，如点击按钮、显示图片等
 */
@Entity
@Table(name = "sys_functional_operation")
public class SysFunctionalOperation extends SysBaseModel {

  /**
   * 父节点
   */
  @PrimaryKeyJoinColumn
  @Column(name = "parent_id", columnDefinition = "varchar(32) comment '父节点'", length = 32)
  private String parentId;

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

}
