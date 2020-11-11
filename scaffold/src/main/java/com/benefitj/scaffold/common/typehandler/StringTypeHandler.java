package com.benefitj.scaffold.common.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * String类型的处理器
 *
 * @param <T>
 * @author DINGXIUAN
 */
public abstract class StringTypeHandler<T extends StringTypeHandler.StringTypeValue> extends BaseTypeHandler<T> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i,
                                  T parameter, JdbcType jdbcType) throws SQLException {
    ps.setString(i, parameter.getValue());
  }

  @Override
  public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return convert(rs.getString(columnName));
  }

  @Override
  public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return convert(rs.getString(columnIndex));
  }

  @Override
  public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return convert(cs.getString(columnIndex));
  }

  /**
   * 转换
   *
   * @param value 值
   * @return 返回转换后的对象
   */
  public abstract T convert(String value);

  /**
   * String类型处理器
   *
   * @author DINGXIUAN
   */
  public interface StringTypeValue extends TypeValue<String> {
    // ~
  }
}
