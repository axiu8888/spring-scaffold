package com.benefitj.system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 操作日志
 */
@Entity
@Table(name = "sys_operation_log")
public class SysOperationLog extends SysBaseModel {

  /**
   * 模块
   */
  @Column(name = "module", columnDefinition = "varchar(50) comment '操作'", length = 50)
  private String module;
  /**
   * 操作
   */
  @Column(name = "operation", columnDefinition = "varchar(100) comment '操作'", length = 100)
  private String operation;
  /**
   * IP地址
   */
  @Column(name = "ip_addr", columnDefinition = "varchar(50) comment 'IP地址'", length = 50)
  private String ipAddr;
  /**
   * 请求路径
   */
  @Column(name = "url", columnDefinition = "varchar(1024) comment '地址'", length = 1024)
  private String url;
  /**
   * 请求方法
   */
  @Column(name = "http_method", columnDefinition = "varchar(20) comment '请求方法'", length = 20)
  private String httpMethod;
  /**
   * 请求参数
   */
  @Column(name = "args", columnDefinition = "text comment '参数'")
  private String args;
  /**
   * Class和Method
   */
  @Column(name = "class_method", columnDefinition = "varchar(100) comment '参数'")
  private String classMethod;
  /**
   * 结果信息
   */
  @Column(name = "result_msg")
  private String resultMsg;
  /**
   * 结果码，一般200是成功，400是错误，500是服务器异常
   */
  @Column(name = "result_code")
  private Integer resultCode;

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public String getIpAddr() {
    return ipAddr;
  }

  public void setIpAddr(String ipAddr) {
    this.ipAddr = ipAddr;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public void setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
  }

  public String getArgs() {
    return args;
  }

  public void setArgs(String args) {
    this.args = args;
  }

  public String getClassMethod() {
    return classMethod;
  }

  public void setClassMethod(String classMethod) {
    this.classMethod = classMethod;
  }

  public String getResultMsg() {
    return resultMsg;
  }

  public void setResultMsg(String resultMsg) {
    this.resultMsg = resultMsg;
  }

  public Integer getResultCode() {
    return resultCode;
  }

  public void setResultCode(Integer resultCode) {
    this.resultCode = resultCode;
  }
}
