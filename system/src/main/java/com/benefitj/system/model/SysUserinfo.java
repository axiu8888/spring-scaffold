package com.benefitj.system.model;

import com.benefitj.scaffold.common.Gender;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户信息
 */
@Entity
@Table(name = "sys_userinfo")
public class SysUserinfo extends BaseModel {

  /**
   * 用户ID
   */
  @Id
  @Column(name = "id", columnDefinition = "varchar(32) comment '用户ID'")
  private String id;
  /**
   * 用户ID
   */
  @Column(name = "user_id", columnDefinition = "varchar(32) comment '用户ID'", nullable = false, unique = true)
  private String userId;
  /**
   * 姓名
   */
  @Column(name = "name", columnDefinition = "varchar(100) comment '姓名'", length = 100)
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
   * 身高
   */
  @Column(name = "height", columnDefinition = "integer comment '身高'")
  private Integer height;
  /**
   * 体重
   */
  @Column(name = "weight", columnDefinition = "float comment '体重'")
  private Float weight;
  /**
   * 头像地址
   */
  @Column(name = "avatar", columnDefinition = "varchar(200) comment '头像'")
  private String avatar;

  public String getId() {
    return id;
  }

  public void setId(String zid) {
    this.id = zid;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
