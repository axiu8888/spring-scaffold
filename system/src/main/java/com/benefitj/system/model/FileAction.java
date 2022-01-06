package com.benefitj.system.model;

/**
 * 文件操作
 */
public enum FileAction implements IAction {

  VIEW(Action.VIEW, "查看"),
  ADD(Action.ADD, "添加"),
  DELETE(Action.DELETE, "删除"),
  EDIT(Action.EDIT, "编辑"),
  ;

  /**
   * 类型
   */
  private final Action type;
  /**
   * 名称
   */
  private final String name;

  FileAction(Action type, String name) {
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
