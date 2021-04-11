package com.benefitj.scaffold;

import com.benefitj.scaffold.mapper.SuperMapper;
import com.benefitj.scaffold.page.OrderUtils;
import com.benefitj.scaffold.page.PageableRequest;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Id;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * base service
 *
 * @param <T>
 * @param <M>
 */
public abstract class BaseService<T, M extends SuperMapper<T>> {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * Mybatis SQL operation
   */
  private SqlSessionTemplate template;

  public BaseService() {
  }

  /**
   * mapper
   */
  protected abstract M getMapper();

  protected SqlSessionTemplate getTemplate() {
    return template;
  }

  @Autowired
  protected void setTemplate(SqlSessionTemplate template) {
    this.template = template;
  }

  /**
   * 默认的实体类
   */
  protected Class<T> getEntityClass() {
    return getMapper().getEntityClass();
  }

  /**
   * 保存记录
   *
   * @param record 记录
   * @return 返回保存的条数
   */
  @Transactional(rollbackFor = Throwable.class)
  public int insert(T record) {
    return getMapper().insert(record);
  }

  /**
   * 批量插入数据
   *
   * @param records 记录集合
   * @return 返回插入条数
   */
  @Transactional(rollbackFor = Throwable.class)
  public int insertAll(Collection<T> records) {
    return getMapper().insertBatch(records);
  }

  /**
   * 通过主键更新
   *
   * @param record 记录
   * @return 更新的条数
   */
  @Transactional(rollbackFor = Throwable.class)
  public int updateByPK(T record) {
    return getMapper().updateByPrimaryKey(record);
  }

  /**
   * 通过主键更新，null值不更新
   *
   * @param record 记录
   * @return 更新的条数
   */
  @Transactional(rollbackFor = Throwable.class)
  public int updateByPKSelective(T record) {
    return getMapper().updateByPrimaryKeySelective(record);
  }

  /**
   * 通过主键查询
   *
   * @param pk 主键
   * @return 返回查询到的记录
   */
  public T getByPK(Object pk) {
    return pk != null ? getMapper().selectByPrimaryKey(pk) : null;
  }

  /**
   * 通过条件查询一条数据
   *
   * @param condition 条件
   * @return 返回查询到的数据
   */
  public T getOne(@Nonnull T condition) {
    return getMapper().selectOne(condition);
  }

  /**
   * 通过条件查询全部数据
   *
   * @param condition 条件
   * @return 返回查询到的全部数据
   */
  public List<T> getAll(@Nullable T condition) {
    return condition != null ? getMapper().select(condition) : getMapper().selectAll();
  }

  /**
   * 查询列表
   *
   * @param condition  条件
   * @param startTime  开始时间
   * @param endTime    结束时间
   * @param multiLevel 是否为多层级
   * @return 返回查询的列表
   */
  public List<T> getList(T condition, @Nullable Date startTime, @Nullable Date endTime, Boolean multiLevel) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("还未实现，请实现此方法!");
  }

  /**
   * 获取列表分页
   *
   * @param page 分页参数
   * @return 返回查询的分页
   */
  public PageInfo<T> getPage(@Nonnull PageableRequest<T> page) {
    // ORDER BY
    String orderBy = String.join(",", getOrderByList(page.getOrderBy()));
    Date startTime = page.getStartTime();
    Date endTime = page.getEndTime();
    // 分页
    PageHelper.startPage(page.getPageNum(), page.getPageSize(), orderBy);
    List<T> list = getList(page.getCondition(), startTime, endTime, page.isMultiLevel());
    return PageInfo.of(list);
  }

  /**
   * 统计条数
   *
   * @param condition 条件
   * @return 返回统计的条数
   */
  public int count(@Nonnull T condition) {
    return getMapper().selectCount(condition);
  }

  /**
   * 通过主键统计条数
   *
   * @param pkValues 主键值(属性, 值)
   * @return 返回统计的条数
   */
  public int countByPK(@Nonnull Map<String, Object> pkValues) {
    return getMapper().selectCountByPK(pkValues);
  }

  /**
   * 通过主键统计条数
   *
   * @param pk 主键
   * @return 返回统计的条数
   */
  public int countByPK(@Nonnull Object pk) {
    EntityColumn pkColumn = PK_CACHE.computeIfAbsent(getEntityClass(), PK_COLUMN_SEARCHER);
    Sqls sqls = Sqls.custom().andEqualTo(pkColumn.getColumn(), pk);
    final M mapper = getMapper();
    return mapper.selectCountByExample(mapper.example(sqls));
  }

  /**
   * 删除记录
   *
   * @param record 记录
   * @return 返回删除的条数
   */
  @Transactional(rollbackFor = Throwable.class)
  public int delete(@Nonnull T record) {
    return getMapper().delete(record);
  }

  /**
   * 通过主键删除记录
   *
   * @param pk 主键
   * @return 返回删除的条数
   */
  @Transactional(rollbackFor = Throwable.class)
  public int deleteByPK(@Nonnull Object pk) {
    return getMapper().deleteByPrimaryKey(pk);
  }

  /**
   * 获取真正的排序字段
   *
   * @param orders 排序列表
   * @return 返回排序字段
   */
  public List<String> getOrderByList(List<String> orders) {
    return getOrderByList(orders, getEntityClass());
  }

  /**
   * 获取真正的排序字段
   *
   * @param orders      排序列表
   * @param entityClass 实体类
   * @return 返回排序字段
   */
  public static List<String> getOrderByList(List<String> orders, Class<?> entityClass) {
    if (orders != null && !orders.isEmpty()) {
      try {
        final EntityTable table = EntityHelper.getEntityTable(entityClass);
        final Map<String, EntityColumn> propertyMap = table.getPropertyMap();
        return OrderUtils.convert(orders)
            .stream()
            .filter(order -> propertyMap.containsKey(order.getProperty()))
            .map(order -> {
              String column = propertyMap.get(order.getProperty()).getColumn();
              order.setColumn(column);
              return order.toString();
            })
            .collect(Collectors.toList());
      } catch (MapperException ignore) {/* ignore */}
    }
    return Collections.emptyList();
  }

  /**
   * 主键缓存
   */
  private static final Map<Class<?>, EntityColumn> PK_CACHE = new WeakHashMap<>();
  /**
   * 查找唯一主键
   */
  private static final Function<Class<?>, EntityColumn> PK_COLUMN_SEARCHER = clazz -> {
    EntityTable et = EntityHelper.getEntityTable(clazz);
    Set<EntityColumn> set = et.getEntityClassPKColumns();
    if (set.isEmpty()) {
      throw new LogicException("无法匹配主键");
    }
    for (EntityColumn column : set) {
      if (column.getEntityField().isAnnotationPresent(Id.class)) {
        return column;
      }
    }
    return set.iterator().next();
  };

}
