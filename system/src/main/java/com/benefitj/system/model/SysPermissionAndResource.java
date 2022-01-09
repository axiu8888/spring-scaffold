package com.benefitj.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 权限和文件资源关联表
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
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
  @Column(name = "permission_id", columnDefinition = "varchar(32) comment '权限ID'", length = 32, nullable = false)
  private String permissionId;
  /**
   * 资源ID
   */
  @Column(name = "resource_id", columnDefinition = "varchar(32) comment '资源ID'", length = 32, nullable = false)
  private String resourceId;
  /**
   * 资源类型
   */
  @Column(name = "type", columnDefinition = "varchar(50) comment '资源类型'", length = 50, nullable = false)
  private String type;

  public ResourceType getResourceType() {
    return ResourceType.of(getType());
  }

  public void setResourceType(ResourceType resourceType) {
    setType(resourceType != null ? resourceType.getName() : getType());
  }

}
