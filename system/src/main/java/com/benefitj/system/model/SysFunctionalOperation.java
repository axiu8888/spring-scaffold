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
 * 功能性操作，如点击按钮、显示图片等
 */
@ApiModel("功能性操作，如点击按钮、显示图片等")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_functional_operation")
public class SysFunctionalOperation extends SysBaseModel {

  /**
   * 父节点
   */
  @ApiModelProperty("父节点")
  @PrimaryKeyJoinColumn
  @Column(name = "parent_id", columnDefinition = "varchar(32) comment '父节点'", length = 32)
  private String parentId;

}
