package com.benefitj.system.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
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
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  @Column(name = "create_time", columnDefinition = "datetime comment '创建时间'")
  private Date createTime;
  /**
   * 修改时间
   */
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  @Column(name = "update_time", columnDefinition = "datetime comment '修改时间'")
  private Date updateTime;
  /**
   * 是否激活
   */
  @Column(name = "active", columnDefinition = "tinyint(1) NOT NULL DEFAULT 1  comment '是否激活'")
  private Boolean active;

}
