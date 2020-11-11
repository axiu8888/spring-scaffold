package com.benefitj.system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 文件资源
 */
@Entity
@Table(name = "sys_file_resource")
public class SysFileResource extends SysBaseModel {

  /**
   * 资源地址
   */
  @Column(name = "uri", columnDefinition = "varchar(1024) comment '资源地址'", length = 1024, nullable = false)
  private String uri;

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

}
