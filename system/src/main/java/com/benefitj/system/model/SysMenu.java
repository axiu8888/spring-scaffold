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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 系统菜单
 */
@ApiModel("系统菜单")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_menu")
public class SysMenu extends SysBaseModel {
  /**
   * 父节点
   */
  @ApiModelProperty("父节点")
  @PrimaryKeyJoinColumn
  @Column(name = "parent_id", columnDefinition = "varchar(32) comment '父节点'", length = 32)
  private String parentId;
  /**
   * 菜单图标
   */
  @ApiModelProperty("菜单图标")
  @Column(name = "icon", columnDefinition = "varchar(1024) comment '菜单图标'", length = 1024)
  private String icon;
  /**
   * 地址
   */
  @ApiModelProperty("地址")
  @Column(name = "uri", columnDefinition = "varchar(1024) comment '地址'", length = 1024)
  private String uri;

}
