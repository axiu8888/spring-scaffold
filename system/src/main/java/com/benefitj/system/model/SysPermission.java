package com.benefitj.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统权限
 */
@ApiModel("系统权限")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_permission")
public class SysPermission extends SysBaseModel {

  /**
   * 资源类型，请参考：{@link ResourceType}
   */
  @ApiModelProperty("资源类型")
  @Column(name = "resource_type", columnDefinition = "varchar(30) comment '资源类型'")
  private String resourceType;

}
