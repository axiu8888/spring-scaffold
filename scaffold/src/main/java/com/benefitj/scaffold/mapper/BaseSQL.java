package com.benefitj.scaffold.mapper;

import com.benefitj.core.DateFmtter;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.util.Collection;
import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class BaseSQL extends SQL {

  private EntityTable table;

  public BaseSQL(Class<?> entityType) {
    this(EntityHelper.getEntityTable(entityType));
  }

  public BaseSQL(EntityTable table) {
    this.table = table;
  }

  public EntityTable getTable() {
    return table;
  }

  public void setTable(EntityTable table) {
    this.table = table;
  }

  public String getTableName() {
    return getTable().getName();
  }

  public String[] getColumns() {
    return getTable().getEntityClassColumns()
        .stream()
        .map(EntityColumn::getColumn)
        .toArray(String[]::new);
  }

  public String fmt(Object time) {
    return DateFmtter.fmt(time, "yyyy-MM-dd HH:mm:ss");
  }

  public String fmtDate(Object time) {
    return DateFmtter.fmt(time, "yyyy-MM-dd");
  }

  /**
   * 初始化基本的SQL
   *
   * @return 返回当前对象
   */
  public BaseSQL SELECT() {
    SELECT(checkNotBlank(EntityHelper.getSelectColumns(getTable().getEntityClass()), "*"));
    return this;
  }

  /**
   * 介于两个时间段之间
   *
   * @param timeColumn 时间字段
   * @param startTime  开始时间
   * @param endTime    结束时间
   * @return 返回当前对象
   */
  public BaseSQL WHERE(String timeColumn, Date startTime, Date endTime) {
    notNull(startTime, () -> WHERE(timeColumn + " >= '" + fmt(startTime) + "'"));
    notNull(endTime, () -> WHERE(timeColumn + " <= '" + fmt(endTime) + "'"));
    return this;
  }

  /**
   * 不为 NULL
   */
  public BaseSQL notNull(Object o, Runnable r) {
    if (o != null) {
      r.run();
    }
    return this;
  }

  /**
   * 检查不为空或空字符串
   *
   * @param str          字符串
   * @param defaultValue 默认值
   * @return 返回合法的值
   */
  public static <T extends CharSequence> T checkNotBlank(T str, T defaultValue) {
    return StringUtils.isNotBlank(str) ? str : defaultValue;
  }

  /**
   * 检查不为空或空字符串
   *
   * @param str  字符串
   * @param task 处理函数
   */
  public static <T extends CharSequence> void notBlank(T str, Consumer<T> task) {
    if (StringUtils.isNotBlank(str)) {
      task.accept(str);
    }
  }

  /**
   * 不为空
   */
  public BaseSQL notEmpty(Collection<?> c, Runnable r) {
    if (c != null && !c.isEmpty()) {
      r.run();
    }
    return this;
  }

  /**
   * 自定义
   */
  public BaseSQL custom(Supplier<Boolean> supplier, Runnable r) {
    if (Boolean.TRUE.equals(supplier.get())) {
      r.run();
    }
    return this;
  }

}
