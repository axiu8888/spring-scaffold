package com.benefitj.system.service;

import com.benefitj.core.IdUtils;
import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.security.exception.PermissionException;
import com.benefitj.system.mapper.SysMenuMapper;
import com.benefitj.system.model.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 菜单
 */
@Service
public class SysMenuService extends SysBaseService<SysMenu, SysMenuMapper> {

  @Autowired
  private SysMenuMapper mapper;

  @Override
  protected SysMenuMapper getMapper() {
    return mapper;
  }

  public boolean checkMenuPermission(SysMenu menu) throws PermissionException {
    if (!super.checkPermission(menu)) {
      logger.info("当前用户无权操作此机构的菜单, checkId: {}, standardId: {}", menu.getOrgId(), currentOrgId());
      throw new PermissionException("当前用户无权操作此菜单");
    }
    return false;
  }

  @Override
  protected void checkRecord(SysMenu record) {
    Checker.requireNotBlank(record.getName(), "菜单名不能为空");
    Checker.requireNotBlank(record.getUri(), "菜单路径不能为空");
  }

  /**
   * 获取菜单
   *
   * @param id 菜单ID
   * @return 返回菜单
   */
  public SysMenu get(String id) {
    SysMenu menu = getByPK(id);
    return menu != null && checkPermission(menu) ? menu : null;
  }

  /**
   * 创建菜单
   *
   * @param menu 菜单信息
   */
  public SysMenu create(SysMenu menu) {
    requireOrgExist(menu.getOrgId());
    checkMenuPermission(menu);
    menu.setId(IdUtils.uuid());
    menu.setOrgId(Checker.checkNotBlank(menu.getOrgId(), currentOrgId()));
    menu.setCreatorId(currentUserId());
    menu.setCreateTime(new Date());
    menu.setActive(Boolean.TRUE);
    super.insert(menu);
    return menu;
  }

  /**
   * 更新菜单
   *
   * @param menu 菜单信息
   * @return 返回更新的数据
   */
  public SysMenu update(SysMenu menu) {
    SysMenu existMenu = getByPK(menu.getId());
    if (existMenu == null) {
      throw new IllegalStateException("无法发现菜单");
    }
    checkMenuPermission(menu);

    existMenu.setName(menu.getName());
    existMenu.setRemarks(menu.getRemarks());
    existMenu.setUpdateTime(new Date());
    super.updateByPKSelective(existMenu);
    return existMenu;
  }

  /**
   * 删除菜单
   *
   * @param id 菜单ID
   * @return 返回删除条数，如果被删除成功，应该返回 1, 否则返回 0
   */
  public int delete(String id, boolean force) {
    SysMenu menu = getByPK(id);
    if (menu != null) {
      checkMenuPermission(menu);
      return super.deleteByPK(menu.getId());
    }
    return 0;
  }

  /**
   * 改变菜单可用状态
   *
   * @param id     菜单ID
   * @param active 状态
   * @return 返回是否更新
   */
  public boolean changeActive(String id, Boolean active) {
    SysMenu menu = get(id);
    if (menu != null) {
      checkMenuPermission(menu);

      menu.setActive(Checker.checkNotNull(active, menu.getActive()));
      menu.setUpdateTime(new Date());
      return updateByPKSelective(menu) > 0;
    }
    return false;
  }

}
