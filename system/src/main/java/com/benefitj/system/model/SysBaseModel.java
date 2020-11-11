package com.benefitj.system.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * 关联机构的基类
 */
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

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String getOrgId() {
    return orgId;
  }

  @Override
  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }
}
