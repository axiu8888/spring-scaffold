package com.benefitj.system.model;

import javax.annotation.Nullable;
import java.util.*;

/**
 * 资源支持的操作类型
 */
public enum Action {

  /**
   * 查看 -> 1
   */
  VIEW(1, "VIEW", "查看"),
  /**
   * 添加 -> 2
   */
  ADD(1 << 1, "ADD", "添加"),
  /**
   * 删除 -> 4
   */
  DELETE(1 << 2, "DELETE", "删除"),
  /**
   * 编辑 -> 8
   */
  EDIT(1 << 3, "EDIT", "编辑"),
  /**
   * 授权 -> 16
   */
  AUTH(1 << 4, "AUTH", "授权"),
  /**
   * 移动 -> 32
   */
  MOVE(1 << 5, "MOVE", "移动"),
  /**
   * 可点击 -> 64
   */
  CLICK(1 << 6, "CLICK", "点击"),
  /**
   * 全部 -> 2147483647
   */
  ALL((1 << 31) - 1, "ALL", "全部");

  private static final Map<Action, String> ACTION_JSON;

  static {
    Map<Action, String> jsonMap = new TreeMap<>(Comparator.comparingInt(Action::getValue));
    for (Action action : values()) {
      String json = "{"
          + "\"value\":" + action.getValue()
          + ",\"name\":\"" + action.getName() + "\""
          + ",\"nameCN\":\"" + action.getNameCN() + "\""
          + "}";
      jsonMap.put(action, json);
    }
    ACTION_JSON = Collections.unmodifiableMap(jsonMap);
  }

  private final int value;
  private final String name;
  private final String nameCN;

  Action(int value, String name, String nameCN) {
    this.value = value;
    this.name = name;
    this.nameCN = nameCN;
  }

  /**
   * 获取 ActionEnum
   *
   * @param name 名称
   * @return 返回对应的 ActionEnum
   */
  @Nullable
  public static Action of(String name) {
    for (Action value : values()) {
      if (value.getName().equalsIgnoreCase(name)) {
        return value;
      }
    }
    return null;
  }

  /**
   * 获取 ActionEnum
   *
   * @param value 值
   * @return 返回对应的 ActionEnum
   */
  @Nullable
  public static Action of(int value) {
    for (Action action : values()) {
      if (action.getValue() == value) {
        return action;
      }
    }
    return null;
  }

  /**
   * 是否具有某个操作
   *
   * @param value  操作值
   * @param action 操作
   * @return 如果是返回true
   */
  public static boolean has(int value, Action action) {
    return ((action.getValue() & value) == action.getValue())
        || ((value & ALL.getValue()) == ALL.getValue());
  }

  /**
   * 是否具有某全部的操作
   *
   * @param value   操作值
   * @param actions 操作
   * @return 如果是返回true
   */
  public static boolean hasAll(int value, Action... actions) {
    for (Action action : actions) {
      if (!has(value, action)) {
        return false;
      }
    }
    return true;
  }

  /**
   * 是否具有某种操作
   *
   * @param value   操作值
   * @param actions 操作
   * @return 如果是返回true
   */
  public static boolean hasAny(int value, Action... actions) {
    for (Action action : actions) {
      if (has(value, action)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 聚合操作的值
   *
   * @param actions 操作
   * @return 返回聚合的值
   */
  public static int aggregate(Collection<Action> actions) {
    int value = 0;
    for (Action action : actions) {
      value |= action.getValue();
    }
    return value;
  }

  /**
   * 聚合操作的值
   *
   * @param actions 操作
   * @return 返回聚合的值
   */
  public static int aggregate(Action... actions) {
    return aggregate(Arrays.asList(actions));
  }

  /**
   * 分离操作
   *
   * @param value 操作值
   * @return 返回全部权限
   */
  public static Set<Action> separate(int value) {
    final Set<Action> actions = new HashSet<>();
    for (Action a : values()) {
      Action action = of(a.getValue() & value);
      if (action != null) {
        actions.add(action);
      }
    }
    return actions;
  }

  public static String getJson(Action action) {
    return ACTION_JSON.get(action);
  }

  public int getValue() {
    return value;
  }

  public String getName() {
    return name;
  }

  public String getNameCN() {
    return nameCN;
  }

}
