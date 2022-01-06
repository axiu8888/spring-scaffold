package com.benefitj.system.model;

/**
 * 菜单操作
 */
public enum MenuAction implements IAction {

  VIEW(Action.VIEW, "查看"),
  ADD(Action.ADD, "添加"),
  DELETE(Action.DELETE, "删除"),
  EDIT(Action.EDIT, "编辑"),
  CLICK(Action.CLICK, "点击"),
  ;

  /**
   * 类型
   */
  private final Action type;
  /**
   * 名称
   */
  private final String name;

  MenuAction(Action type, String name) {
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
