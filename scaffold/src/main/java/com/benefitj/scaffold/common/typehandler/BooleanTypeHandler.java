package com.benefitj.scaffold.common.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 布尔类型的处理器
 *
 * @param <T>
 * @author DINGXIUAN
 */
public abstract class BooleanTypeHandler<T extends BooleanTypeHandler.BooleanTypeValue> extends BaseTypeHandler<T> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i,
                                  T parameter, JdbcType jdbcType) throws SQLException {
    ps.setBoolean(i, parameter.getValue());
  }

  @Override
  public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return convert(rs.getBoolean(columnName));
  }

  @Override
  public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return convert(rs.getBoolean(columnIndex));
  }

  @Override
  public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return convert(cs.getBoolean(columnIndex));
  }

  /**
   * 转换器
   *
   * @param value
   * @return
   */
  public abstract T convert(boolean value);

  /**
   * 整形类型处理器
   *
   * @author DINGXIUAN
   */
  public interface BooleanTypeValue extends TypeValue<Boolean> {
    // ~
  }
}
