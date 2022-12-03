package com.benefitj.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 操作日志
 */
@ApiModel("菜单图标")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "sys_operation_log")
public class SysOperationLog extends SysBaseModel {

  /**
   * 模块
   */
  @ApiModelProperty("模块")
  @Column(name = "module", columnDefinition = "varchar(50) comment '操作'", length = 50)
  private String module;
  /**
   * 操作
   */
  @ApiModelProperty("操作")
  @Column(name = "operation", columnDefinition = "varchar(100) comment '操作'", length = 100)
  private String operation;
  /**
   * IP地址
   */
  @ApiModelProperty("IP地址")
  @Column(name = "ip_addr", columnDefinition = "varchar(50) comment 'IP地址'", length = 50)
  private String ipAddr;
  /**
   * 请求路径
   */
  @ApiModelProperty("请求路径")
  @Column(name = "url", columnDefinition = "varchar(1024) comment '地址'", length = 1024)
  private String url;
  /**
   * 请求方法
   */
  @ApiModelProperty("请求方法")
  @Column(name = "http_method", columnDefinition = "varchar(20) comment '请求方法'", length = 20)
  private String httpMethod;
  /**
   * 请求参数
   */
  @ApiModelProperty("请求参数")
  @Column(name = "args", columnDefinition = "text comment '参数'")
  private String args;
  /**
   * Class和Method
   */
  @ApiModelProperty("Class和Method")
  @Column(name = "class_method", columnDefinition = "varchar(100) comment '参数'")
  private String classMethod;
  /**
   * 结果信息
   */
  @ApiModelProperty("结果信息")
  @Column(name = "result_msg")
  private String resultMsg;
  /**
   * 结果码，一般200是成功，400是错误，500是服务器异常
   */
  @ApiModelProperty("结果码，一般200是成功，400是错误，500是服务器异常")
  @Column(name = "result_code")
  private Integer resultCode;

}
