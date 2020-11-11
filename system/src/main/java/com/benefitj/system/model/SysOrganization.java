package com.benefitj.system.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * 系统菜单
 */
@Entity
@Table(name = "sys_organization")
public class SysOrganization extends BaseModel implements ISysBaseModel<String> {

  public static final String AUTO = ":";

  /**
   * 机构 ID
   */
  @Id
  @Column(name = "id", columnDefinition = "varchar(32) comment '机构ID'", length = 32)
  private String id;
  /**
   * 父级机构 ID
   */
  @PrimaryKeyJoinColumn
  @Column(name = "parent_id", columnDefinition = "varchar(32) comment '父级机构ID'", length = 32)
  private String parentId;
  /**
   * 机构名
   */
  @Column(name = "name", columnDefinition = "varchar(100) comment '机构名'", length = 100)
  private String name;
  /**
   * code或id 生成的路径，包含自身的code或id(具体取什么值根据业务决定)
   */
  @PrimaryKeyJoinColumn
  @Column(name = "auto_code", columnDefinition = "varchar(1024) comment '生成的路径'", length = 1024)
  private String autoCode;
  /**
   * 机构号
   */
  @Column(name = "code", columnDefinition = "varchar(30) comment '机构号'", length = 30)
  private String code;
  /**
   * 机构LOGO
   */
  @Column(name = "logo", columnDefinition = "varchar(1024) comment '机构LOGO'", length = 1024)
  private String logo;

  public SysOrganization() {
  }

  public SysOrganization(String id) {
    this.id = id;
    this.setActive(null);
  }

  /**
   * 生成 autoCode
   *
   * @param parentAutoCode 父节点的 autoCode
   * @param code           code 或 id
   * @return 返回生成的 autoCode
   */
  public static String generateAutoCode(String parentAutoCode, String code) {
    return isNotEmpty(parentAutoCode) ? (parentAutoCode + ":" + code) : code;
  }

  /**
   * 获取父节点的 autoCode
   *
   * @param autoCode 当前的 autoCode
   * @return 返回父节点的 autoCode
   */
  public static String getParentAutoCode(String autoCode) {
    return isNotEmpty(autoCode) && autoCode.contains(AUTO)
        ? autoCode.substring(0, autoCode.lastIndexOf(AUTO)) : autoCode;
  }

  private static boolean isNotEmpty(String str) {
    return str != null && !str.isEmpty();
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  public String getAutoCode() {
    return autoCode;
  }

  public void setAutoCode(String autoCode) {
    this.autoCode = autoCode;
  }

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  /**
   * 获取机构的ID
   */
  @JSONField(serialize = false)
  @JsonIgnore
  @Override
  public String getOrgId() {
    return getId();
  }

  @JSONField(serialize = false)
  @JsonIgnore
  @Override
  public void setOrgId(String orgId) {
    this.setId(orgId);
  }

  /**
   * 获取机构名称
   */
  @JSONField(serialize = false)
  @JsonIgnore
  public String getOrgName() {
    return getName();
  }

  /**
   * 获取机构编码
   */
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  /**
   * 获取父节点的 autoCode
   */
  @JsonIgnore
  public String getParentAutoCode() {
    String autoCode = getAutoCode();
    return getParentAutoCode(autoCode);
  }
}
