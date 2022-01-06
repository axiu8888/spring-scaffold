package com.benefitj.system.model;

/**
 * 按钮的操作
 */
public enum ButtonAction implements IAction {

  VIEW(Action.VIEW, "查看"),
  ADD(Action.ADD, "添加"),
  DELETE(Action.DELETE, "删除"),
  CLICK(Action.CLICK, "可点击"),
  ;

  /**
   * 类型
   */
  private final Action type;
  /**
   * 名称
   */
  private final String name;

  ButtonAction(Action type, String name) {
    this.type = type;
    this.name = name;
  }

  @Override
  public Action getType() {
    return type;
  }

  @Override
  public String getName() {
    return name;
  }

}
