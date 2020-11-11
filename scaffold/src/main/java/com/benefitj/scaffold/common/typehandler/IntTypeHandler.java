package com.benefitj.scaffold.common.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 整形枚举类型的处理器
 *
 * @author DINGXIUAN
 */
public abstract class IntTypeHandler<T extends IntTypeHandler.IntTypeValue> extends BaseTypeHandler<T> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i,
                                  T parameter, JdbcType jdbcType) throws SQLException {
    ps.setInt(i, parameter.getValue());
  }

  @Override
  public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return convert(rs.getInt(columnName));
  }

  @Override
  public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return convert(rs.getInt(columnIndex));
  }

  @Override
  public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return convert(cs.getInt(columnIndex));
  }

  /**
   * 转换器
   *
   * @param value
   * @return
   */
  public abstract T convert(int value);

  /**
   * 整形类型处理器
   *
   * @author DINGXIUAN
   */
  public interface IntTypeValue extends TypeValue<Integer> {
    // ~
  }
}
