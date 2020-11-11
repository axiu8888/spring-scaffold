package com.benefitj.system.model;

import javax.persistence.*;

/**
 * 权限和文件资源关联表
 */
@Entity
@Table(name = "sys_permission_and_resource")
public class SysPermissionAndResource {

  /**
   * ID
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /**
   * 权限ID
   */
  @PrimaryKeyJoinColumn
  @Column(name = "permission_id", columnDefinition = "varchar(32) comment '权限ID'", length = 32, nullable = false)
  private String permissionId;
  /**
   * 资源ID
   */
  @PrimaryKeyJoinColumn
  @Column(name = "resource_id", columnDefinition = "varchar(32) comment '资源ID'", length = 32, nullable = false)
  private String resourceId;
  /**
   * 资源类型
   */
  @PrimaryKeyJoinColumn
  @Column(name = "type", columnDefinition = "varchar(50) comment '资源类型'", length = 50, nullable = false)
  private String type;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPermissionId() {
    return permissionId;
  }

  public void setPermissionId(String permissionId) {
    this.permissionId = permissionId;
  }

  public String getResourceId() {
    return resourceId;
  }

  public void setResourceId(String resourceId) {
    this.resourceId = resourceId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ResourceType getResourceType() {
    return ResourceType.of(getType());
  }

  public void setResourceType(ResourceType resourceType) {
    setType(resourceType != null ? resourceType.getName() : getType());
  }

}
