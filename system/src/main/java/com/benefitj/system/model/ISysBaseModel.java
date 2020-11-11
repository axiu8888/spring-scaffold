package com.benefitj.system.model;

import java.util.Date;

public interface ISysBaseModel<ID> {

  /**
   * 获取ID
   */
  ID getId();

  /**
   * 设置ID
   *
   * @param id ID
   */
  void setId(ID id);

  /**
   * 获取机构ID
   */
  String getOrgId();

  /**
   * 机构ID
   *
   * @param orgId 机构ID
   */
  void setOrgId(String orgId);

  /**
   * 获取名称
   */
  String getName();

  /**
   * 设置名称
   *
   * @param name 名称
   */
  void setName(String name);

  /**
   * 获取设置创建者ID
   */
  String getCreatorId();

  /**
   * 设置创建者
   *
   * @param creatorId 创建者ID
   */
  void setCreatorId(String creatorId);

  /**
   * 获取备注
   */
  String getRemarks();

  /**
   * 设置备注
   *
   * @param remarks 备注
   */
  void setRemarks(String remarks);

  /**
   * 获取创建时间
   */
  Date getCreateTime();

  /**
   * 设置创建时间
   *
   * @param createTime 创建时间
   */
  void setCreateTime(Date createTime);

  /**
   * 获取更新时间
   */
  Date getUpdateTime();

  /**
   * 设置更新时间
   *
   * @param updateTime 更新时间
   */
  void setUpdateTime(Date updateTime);

  /**
   * 获取激活状态
   */
  Boolean getActive();

  /**
   * 设置激活
   *
   * @param active 激活状态
   */
  void setActive(Boolean active);
}
