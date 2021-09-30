package com.benefitj.system.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 字典
 */
@Entity
@Table(name = "sys_dictionary")
public class SysDictionaryModel extends SysBaseModel {

  /**
   * 父级节点
   */
  @Column(name = "parent_id", columnDefinition = "varchar(32) comment '父级节点'", length = 32)
  private String parentId;
  /**
   * 字典代码
   */
  @Column(name = "code", columnDefinition = "varchar(50) comment '字典代码'", length = 50)
  private String code;
  /**
   * 字典属性
   */
  @Column(name = "attribute", columnDefinition = "varchar(50) comment '字典属性'", length = 50)
  private String attribute;
  /**
   * 字典属性值
   */
  @Column(name = "value", columnDefinition = "varchar(255) comment '字典属性值'", length = 255)
  private String value;

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getAttribute() {
    return attribute;
  }

  public void setAttribute(String attribute) {
    this.attribute = attribute;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
