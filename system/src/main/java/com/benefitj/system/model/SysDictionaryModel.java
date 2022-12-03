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
 * 字典
 */
@ApiModel("字典")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_dictionary")
public class SysDictionaryModel extends SysBaseModel {

  /**
   * 父级节点
   */
  @ApiModelProperty("父级节点")
  @Column(name = "parent_id", columnDefinition = "varchar(32) comment '父级节点'", length = 32)
  private String parentId;
  /**
   * 字典代码
   */
  @ApiModelProperty("字典代码")
  @Column(name = "code", columnDefinition = "varchar(50) comment '字典代码'", length = 50)
  private String code;
  /**
   * 字典属性
   */
  @ApiModelProperty("字典属性")
  @Column(name = "attribute", columnDefinition = "varchar(50) comment '字典属性'", length = 50)
  private String attribute;
  /**
   * 字典属性值
   */
  @ApiModelProperty("字典属性值")
  @Column(name = "value", columnDefinition = "varchar(255) comment '字典属性值'", length = 255)
  private String value;

}
