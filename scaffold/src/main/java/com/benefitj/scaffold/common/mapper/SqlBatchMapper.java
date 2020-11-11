package com.benefitj.scaffold.common.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Collection;
import java.util.Set;

/**
 * 批处理操作，主要是批量插入和更新
 *
 * @param <T>
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface SqlBatchMapper<T> {

  /**
   * 插入全部记录
   */
  @Options(useGeneratedKeys = true)
  @InsertProvider(type = SqlBatchProvider.class, method = "dynamicSQL")
  int insertBatch(@Param("list") Collection<? extends T> records);

  /**
   * 更新全部记录
   */
  @UpdateProvider(type = SqlBatchProvider.class, method = "dynamicSQL")
  int updateBatch(@Param("list") Collection<? extends T> records);

  /**
   * 更新全部记录，允许为空
   */
  @UpdateProvider(type = SqlBatchProvider.class, method = "dynamicSQL")
  int updateBatchSelective(@Param("list") Collection<? extends T> records);

  /**
   * 批量插入
   */
  final class SqlBatchProvider extends MapperTemplate {

    public SqlBatchProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
      super(mapperClass, mapperHelper);
    }

    /**
     * 批量插入
     *
     * @param ms
     */
    public String insertBatch(MappedStatement ms) {
      final Class<?> entityClass = getEntityClass(ms);
      // 开始拼sql
      StringBuilder sql = new StringBuilder();
      sql.append(
          "<bind name=\"listNotEmptyCheck\" value=\"@tk.mybatis.mapper.util.OGNL@notEmptyCollectionCheck(list, '")
          .append(ms.getId())
          .append(" 传入的集合参数为空')\"/>");

      sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass), "list[0]"));
      sql.append(SqlHelper.insertColumns(entityClass, false, false, false));

      sql.append(" VALUES ");
      sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
      sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
      // 获取全部列
      Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
      // 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
      for (EntityColumn column : columnList) {
        sql.append(column.getColumnHolder("record")).append(",");
      }
      sql.append("</trim>");
      sql.append("</foreach>");

      // 反射把MappedStatement中的设置主键名
      EntityHelper.setKeyProperties(EntityHelper.getPKColumns(entityClass), ms);

      return sql.toString();
    }

    /**
     * 批量更新
     *
     * @param ms
     */
    public String updateBatch(MappedStatement ms) {
      return jointUpdateSql(ms, true);
    }

    /**
     * 批量更新
     *
     * @param ms
     */
    public String updateBatchSelective(MappedStatement ms) {
      return jointUpdateSql(ms, false);
    }

    /**
     * 批量更新
     *
     * @param ms
     * @param ignoreNull 忽略null值
     */
    public String jointUpdateSql(MappedStatement ms, boolean ignoreNull) {

      final Class<?> entityClass = getEntityClass(ms);
      // 开始拼sql
      StringBuilder sql = new StringBuilder();
      sql.append("UPDATE ").append(tableName(entityClass)).append(" ");
      sql.append("<trim prefix=\"SET\" suffixOverrides=\",\">");

      Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(entityClass);

      EntityColumn pkColumn = null;
      for (EntityColumn column : pkColumns) {
        pkColumn = column;
        break;
      }

      if (pkColumn == null) {
        throw new IllegalStateException("批量更新需要主键不为空");
      }

      // 获取全部列
      Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
      // 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
      for (EntityColumn column : columnList) {
        if (!column.isId() && column.isInsertable()) {
          sql.append("<trim prefix=\"").append(column.getColumn()).append("=CASE\" suffix=\"END,\">");
          sql.append("<foreach collection=\"list\" item=\"item\" index=\"index\">");
          if (ignoreNull) {
            sql.append("<if test=\"item.").append(pkColumn.getProperty()).append(" != null\">");
          } else {
            sql.append("<if test=\"item.").append(pkColumn.getProperty()).append(" != null and item.").append(column.getProperty()).append(" != null\">");
          }
          sql.append(" WHEN ").append(pkColumn.getColumn()).append("=#{item.").append(pkColumn.getProperty()).append("}");
          sql.append(" THEN #{item.").append(column.getProperty()).append("}");
          sql.append("</if>");
          sql.append("</foreach>");
          sql.append("</trim>");
        }
      }
      sql.append("</trim>");

      sql.append(" WHERE ");
      sql.append("<foreach collection=\"list\" separator=\"OR\" item=\"item\" index=\"index\">");
      sql.append(pkColumn.getColumn()).append("=#{item.").append(pkColumn.getProperty()).append("}");
      sql.append("</foreach>");

      // 反射把MappedStatement中的设置主键名
      EntityHelper.setKeyProperties(pkColumns, ms);

      return sql.toString();
    }


    /**
     * 插入，主键id，自增
     *
     * @param ms
     */
    public String insertUseGeneratedKeys(MappedStatement ms) {
      final Class<?> entityClass = getEntityClass(ms);
      // 开始拼sql
      StringBuilder sql = new StringBuilder();
      sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
      sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
      sql.append(SqlHelper.insertValuesColumns(entityClass, false, false, false));

      // 反射把MappedStatement中的设置主键名
      EntityHelper.setKeyProperties(EntityHelper.getPKColumns(entityClass), ms);

      return sql.toString();
    }
  }
}

