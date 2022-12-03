package com.benefitj.system.service;

import com.benefitj.scaffold.BaseService;
import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.LogicException;
import com.benefitj.scaffold.security.CurrentUserService;
import com.benefitj.spring.mvc.query.PageRequest;
import com.benefitj.system.mapper.SysBaseMapper;
import com.benefitj.system.model.ISysBaseModel;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

public abstract class SysBaseService<T extends ISysBaseModel, M extends SysBaseMapper<T>>
    extends BaseService<T, M> implements CurrentUserService {

  private SysOrganizationService orgService;

  public SysOrganizationService getOrgService() {
    return orgService;
  }

  @Lazy
  @Autowired
  public void setOrgService(SysOrganizationService orgService) {
    this.orgService = orgService;
  }

  /**
   * 检查机构层级
   *
   * @param orgId 机构ID
   * @return 返回是否可以继续操作
   */
  public boolean checkOrgLevel(String orgId) {
    return currentToken() == null || getOrgService().checkOrgLevel(orgId);
  }

  /**
   * 检查条件中的机构，如果不匹配，修改机构ID
   *
   * @param c 条件
   */
  protected void changeOrgLevel(T c) {
    if (c != null) {
      String orgId = c.getOrgId();
      if (StringUtils.isBlank(orgId)) {
        c.setOrgId(currentOrgId());
      } else {
        // 检查查询的机构是否为当前机构的子级机构
        if (!checkOrgLevel(orgId)) {
          c.setOrgId(currentOrgId());
        }
      }
    }
  }

  /**
   * 检查权限
   *
   * @param t 实体对象
   * @return 是否具有权限
   */
  public boolean checkPermission(T t) {
    if (t != null) {
      return checkOrgLevel(t.getOrgId());
    }
    return true;
  }

  /**
   * 检查机构是否存在，不存在直接抛出异常
   *
   * @param orgId 机构ID
   */
  public void requireOrgExist(String orgId) {
    if (getOrgService().countById(orgId) <= 0) {
      throw new LogicException("无法发现机构");
    }
  }

  @Override
  public int insert(T record) {
    checkRecord(record);
    return super.insert(record);
  }

  @Override
  public int updateByPK(T record) {
    checkRecord(record);
    return super.updateByPK(record);
  }

  @Override
  public int updateByPKSelective(T record) {
    checkRecord(record);
    return super.updateByPKSelective(record);
  }

  protected void checkRecord(T record) {
    // ~
  }

  @Override
  public List<T> getAll(@Nullable T condition) {
    changeOrgLevel(condition);
    return super.getAll(condition);
  }

  @Override
  public List<T> getList(T condition, Date startTime, Date endTime, Boolean multiLevel) {
    return getMapper().selectList(condition, startTime, endTime, multiLevel);
  }

  @Override
  public PageInfo<T> getPage(PageRequest<T> page) {
    page.setCondition(checkAndGetEntity(page.getCondition()));
    changeOrgLevel(page.getCondition());
    return super.getPage(page);
  }

  /**
   * 检查并返回entity，如果没有就创建新的
   *
   * @param entity 实体对象
   * @return 返回实体对象
   */
  protected T checkAndGetEntity(T entity) {
    return Checker.checkNull(entity, () -> {
      final Class<T> entityClass = getEntityClass();
      try {
        return entityClass.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        logger.warn("create entity fail for \"{}\", error: {}", entityClass, e.getMessage());
      }
      return entity;
    });
  }

}
