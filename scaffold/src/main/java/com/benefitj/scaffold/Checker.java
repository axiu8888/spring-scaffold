package com.benefitj.scaffold;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Checker {

  /**
   * 检查为 null
   *
   * @param t    被检查的对象
   * @param task 处理的函数
   * @return 返回合法的值
   */
  public static <T> T checkNull(T t, Supplier<T> task) {
    return t == null ? task.get() : t;
  }

  /**
   * 检查为 null
   *
   * @param t 被检查的对象
   */
  public static <T> void checkNull(T t, Runnable task) {
    if (t == null) {
      task.run();
    }
  }

  /**
   * 检查为 null
   *
   * @param t    被检查的对象
   * @param task 处理的函数
   */
  public static <T> void checkNotNull(T t, Runnable task) {
    if (t != null) {
      task.run();
    }
  }

  /**
   * 检查不为 null
   *
   * @param t    被检查的对象
   * @param task 处理的函数
   * @return 返回合法的值
   */
  public static <T, R> R checkNotNull(T t, Function<T, R> task) {
    return t != null ? task.apply(t) : null;
  }

  /**
   * 检查不为 null
   *
   * @param t            被检查的对象
   * @param defaultValue 默认值
   * @return 返回合法的值
   */
  public static <T> T checkNotNull(T t, T defaultValue) {
    return t != null ? t : defaultValue;
  }

  /**
   * 检查不为空
   *
   * @param str          字符串
   * @param defaultValue 默认值
   * @return 返回合法的值
   */
  public static String checkNotEmpty(String str, String defaultValue) {
    return StringUtils.isNotEmpty(str) ? str : defaultValue;
  }

  /**
   * 检查不为空或空字符串
   *
   * @param str          字符串
   * @param defaultValue 默认值
   * @return 返回合法的值
   */
  public static String checkNotBlank(String str, String defaultValue) {
    return StringUtils.isNotBlank(str) ? str : defaultValue;
  }

  /**
   * 检查不为空或空字符串
   *
   * @param str  字符串
   * @param task 处理函数
   */
  public static void checkNotBlank(String str, Consumer<String> task) {
    if (StringUtils.isNotBlank(str)) {
      task.accept(str);
    }
  }

  /**
   * 检查为空或空字符串
   *
   * @param str  字符串
   * @param task 处理函数
   * @return 返回合法的值
   */
  public static void checkBlank(String str, Runnable task) {
    if (StringUtils.isBlank(str)) {
      task.run();
    }
  }

  public static void requireNotBlank(String str, String errorMsg) {
    if (StringUtils.isBlank(str)) {
      throw new IllegalStateException(errorMsg);
    }
  }

  public static void requireNotNull(Object o, String errorMsg) {
    if (o == null) {
      throw new IllegalStateException(errorMsg);
    }
  }
}
