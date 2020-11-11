package com.benefitj.system.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * 请求记录信息
 */
@Embeddable
@Entity
@Table(name = "sys_request_record")
public class SysRequestRecord extends BaseModel implements ISysBaseModel<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /**
   * 用户ID
   */
  @PrimaryKeyJoinColumn
  @Column(name = "user_id", columnDefinition = "varchar(32) comment '用户ID'", length = 32)
  private String userId;
  /**
   * 机构ID
   */
  @PrimaryKeyJoinColumn
  @Column(name = "org_id", columnDefinition = "varchar(32) comment '机构ID'", length = 32)
  private String orgId;
  /**
   * 请求路径
   */
  @Column(name = "url", columnDefinition = "varchar(1024) comment '请求路径'", length = 1024)
  private String url;
  /**
   * IP地址
   */
  @Column(name = "ip_addr", columnDefinition = "varchar(30) comment 'IP地址'", length = 30)
  private String ipAddr;
  /**
   * 方法：GET, POST, DELETE, PUT, OPTIONS...
   */
  @Column(name = "method", columnDefinition = "varchar(20) comment '方法：GET, POST, DELETE, PUT, OPTIONS...'", length = 20)
  private String method;
  /**
   * 请求参数
   */
  @Column(name = "params", columnDefinition = "varchar(2048) comment '请求参数'", length = 2048)
  private String params;
  /**
   * 结果码
   */
  @Column(name = "result_code", columnDefinition = "integer comment '结果码'")
  private Integer resultCode;
  /**
   * 结果信息
   */
  @Column(name = "result_msg", columnDefinition = "varchar(1024) comment '结果信息'", length = 1024)
  private String resultMsg;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  @Override
  public String getOrgId() {
    return orgId;
  }

  @Override
  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getIpAddr() {
    return ipAddr;
  }

  public void setIpAddr(String ipAddr) {
    this.ipAddr = ipAddr;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public Integer getResultCode() {
    return resultCode;
  }

  public void setResultCode(Integer resultCode) {
    this.resultCode = resultCode;
  }

  public String getResultMsg() {
    return resultMsg;
  }

  public void setResultMsg(String resultMsg) {
    this.resultMsg = resultMsg;
  }

  @JSONField(serialize = false)
  @JsonIgnore
  @Deprecated
  @Override
  public String getName() {
    return null;
  }

  @JSONField(serialize = false)
  @JsonIgnore
  @Deprecated
  @Override
  public void setName(String name) {
  }
}
