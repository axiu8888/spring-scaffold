package com.benefitj.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * 关联机构的基类
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public abstract class SysBaseModel extends BaseModel implements ISysBaseModel<String> {

  /**
   * ID
   */
  @Id
  @Column(name = "id", columnDefinition = "varchar(32) comment 'ID'", length = 32)
  private String id;
  /**
   * 所属机构ID
   */
  @Column(name = "org_id", columnDefinition = "varchar(32) comment '所属机构ID' ", nullable = false, length = 32)
  private String orgId;
  /**
   * 名称
   */
  @PrimaryKeyJoinColumn
  @Column(name = "name", columnDefinition = "varchar(100) comment '名称' ", length = 100)
  private String name;

}
