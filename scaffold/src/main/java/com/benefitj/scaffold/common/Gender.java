package com.benefitj.scaffold.common;

/**
 * 性别
 */
public enum Gender {

  /**
   * 女
   */
  FEMALE("女", 0),
  /**
   * 男
   */
  MALE("男", 1),
  /**
   * 未知
   */
  UNKNOWN("未知", -1);

  private final String name;
  private final int type;

  Gender(String name, int type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public int getType() {
    return type;
  }

  /**
   * 是否为男性
   */
  public boolean isMale() {
    return this == MALE;
  }

  /**
   * 是否为女性
   */
  public boolean isFemale() {
    return this == FEMALE;
  }

}
