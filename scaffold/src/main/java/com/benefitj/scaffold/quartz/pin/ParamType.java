package com.benefitj.scaffold.quartz.pin;

/**
 * 参数类型
 */
public enum ParamType {

  INTEGER("Integer", "整数"),
  FLOAT("Float", "浮点数"),
  BOOLEAN("Boolean", "布尔值"),
  STRING("String", "字符串");

  final String type;
  final String name;

  ParamType(String type, String name) {
    this.type = type;
    this.name = name;
  }

  public java.lang.String getType() {
    return type;
  }

  public java.lang.String getName() {
    return name;
  }
}
