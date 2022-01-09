package com.benefitj.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 机构和用户关联表
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "sys_org_and_user")
public class SysOrgAndUser {

  /**
   * ID
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /**
   * 机构ID
   */
  @Column(name = "org_id", columnDefinition = "varchar(32) comment '机构ID'", length = 32, nullable = false)
  private String orgId;
  /**
   * 用户ID
   */
  @Column(name = "user_id", columnDefinition = "varchar(32) comment '用户ID'", length = 32, nullable = false)
  private String userId;

}
