package com.benefitj.system.model;

import com.benefitj.scaffold.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户信息
 */
@ApiModel("用户信息")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_user")
public class SysUser extends BaseModel implements ISysBaseModel<String> {

  /**
   * 用户ID
   */
  @ApiModelProperty("ID")
  @Id
  @Column(name = "id", columnDefinition = "varchar(32) comment '用户ID'")
  private String id;
  /**
   * 机构ID
   */
  @ApiModelProperty("机构ID")
  @Column(name = "org_id", columnDefinition = "varchar(32) comment '机构ID'")
  private String orgId;
  /**
   * 姓名
   */
  @ApiModelProperty("姓名")
  @Column(name = "name", columnDefinition = "varchar(50) comment '姓'", length = 50)
  private String name;
  /**
   * 性别
   */
  @ApiModelProperty("性别")
  @Column(name = "gender", columnDefinition = "varchar(20) comment '性别'")
  private Gender gender;
  /**
   * 出生日期
   */
  @ApiModelProperty("出生日期")
  @Column(name = "birthday", columnDefinition = "datetime comment '出生日期'")
  private Date birthday;
  /**
   * 身高 CM
   */
  @ApiModelProperty("身高 CM")
  @Column(name = "height", columnDefinition = "integer comment '身高'")
  private Integer height;
  /**
   * 体重 KG
   */
  @ApiModelProperty("体重 KG")
  @Column(name = "weight", columnDefinition = "float comment '体重'")
  private Float weight;
  /**
   * 头像地址
   */
  @ApiModelProperty("头像地址")
  @Column(name = "avatar", columnDefinition = "varchar(200) comment '头像'")
  private String avatar;

}
