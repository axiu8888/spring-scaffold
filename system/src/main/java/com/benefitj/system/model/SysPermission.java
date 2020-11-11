package com.benefitj.system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统权限
 */
@Entity
@Table(name = "sys_permission")
public class SysPermission extends SysBaseModel {

  /**
   * 资源类型，请参考：{@link ResourceType}
   */
  @Column(name = "resource_type", columnDefinition = "varchar(30) comment '资源类型'")
  private String resourceType;

  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

}
