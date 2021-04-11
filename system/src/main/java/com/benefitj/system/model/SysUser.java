package com.benefitj.system.model;

import com.benefitj.scaffold.Gender;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户信息
 */
@Entity
@Table(name = "sys_user")
public class SysUser extends BaseModel implements ISysBaseModel<String> {

  /**
   * 用户ID
   */
  @Id
  @Column(name = "id", columnDefinition = "varchar(32) comment '用户ID'")
  private String id;
  /**
   * 机构ID
   */
  @Column(name = "org_id", columnDefinition = "varchar(32) comment '机构ID'")
  private String orgId;
  /**
   * 姓
   */
  @Column(name = "name", columnDefinition = "varchar(50) comment '姓'", length = 50)
  private String name;
  /**
   * 性别
   */
  @Column(name = "gender", columnDefinition = "varchar(20) comment '性别'")
  private Gender gender;
  /**
   * 出生日期
   */
  @Column(name = "birthday", columnDefinition = "datetime comment '出生日期'")
  private Date birthday;
  /**
   * 身高 CM
   */
  @Column(name = "height", columnDefinition = "integer comment '身高'")
  private Integer height;
  /**
   * 体重 KG
   */
  @Column(name = "weight", columnDefinition = "float comment '体重'")
  private Float weight;
  /**
   * 头像地址
   */
  @Column(name = "avatar", columnDefinition = "varchar(200) comment '头像'")
  private String avatar;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String zid) {
    this.id = zid;
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
  public void setName(String firstName) {
    this.name = firstName;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public Float getWeight() {
    return weight;
  }

  public void setWeight(Float weight) {
    this.weight = weight;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }
}
