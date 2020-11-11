package com.benefitj.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public abstract class BaseModel {

  /**
   * 创建者
   */
  @Column(name = "creator_id", columnDefinition = "varchar(32) comment '创建者'", length = 32)
  private String creatorId;
  /**
   * 备注
   */
  @Column(name = "remarks", columnDefinition = "varchar(1024) comment '备注'", length = 1024)
  private String remarks;
  /**
   * 创建时间
   */
  @JsonIgnore
  @Column(name = "create_time", columnDefinition = "datetime comment '创建时间'")
  private Date createTime;
  /**
   * 修改时间
   */
  @JsonIgnore
  @Column(name = "update_time", columnDefinition = "datetime comment '修改时间'")
  private Date updateTime;
  /**
   * 是否激活
   */
  @Column(name = "active", columnDefinition = "tinyint(1) NOT NULL DEFAULT 1  comment '是否激活'")
  private Boolean active;

  public String getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(String creatorId) {
    this.creatorId = creatorId;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }
}
