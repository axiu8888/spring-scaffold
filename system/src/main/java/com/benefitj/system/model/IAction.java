package com.benefitj.system.model;

public interface IAction {

  /**
   * 类型
   */
  Action getType();

  /**
   * 名称
   */
  String getName();

  /**
   * 是否具有某个操作
   *
   * @param action 操作
   * @return 如果是返回true
   */
  default boolean has(Action action) {
    return Action.has(getType().getValue(), action);
  }

  /**
   * 是否具有某全部的操作
   *
   * @param actions 操作
   * @return 如果是返回true
   */
  default boolean hasAll(Action... actions) {
    return Action.hasAll(getType().getValue(), actions);
  }

  /**
   * 是否具有某种操作
   *
   * @param actions 操作
   * @return 如果是返回true
   */
  default boolean hasAny(Action... actions) {
    return Action.hasAny(getType().getValue(), actions);
  }

}
