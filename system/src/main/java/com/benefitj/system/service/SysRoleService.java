package com.benefitj.system.service;

import com.benefitj.core.IdUtils;
import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.LogicException;
import com.benefitj.scaffold.security.exception.PermissionException;
import com.benefitj.system.mapper.SysRoleMapper;
import com.benefitj.system.model.SysRole;
import com.benefitj.system.model.SysUserAndRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 系统角色
 */
@Service
public class SysRoleService extends SysBaseService<SysRole, SysRoleMapper> {


  @Autowired
  private SysUserAndRoleService uarService;

  @Autowired
  private SysRoleMapper mapper;

  @Override
  protected SysRoleMapper getMapper() {
    return mapper;
  }

  @Override
  public boolean checkOrgLevel(String orgId) {
    return StringUtils.isBlank(orgId) || getOrgService().checkOrgLevel(orgId);
  }

  /**
   * 检查角色权限
   *
   * @param role 角色
   * @throws PermissionException
   */
  public void checkRolePermission(SysRole role) {
    if (currentToken() != null && role != null) {
      if (!checkOrgLevel(role.getOrgId())) {
        logger.info("当前用户无权操作此机构的角色, checkId: {}, standardId: {}", role.getOrgId(), currentOrgId());
        throw new PermissionException("当前用户无权操作此角色");
      }
    }
  }

  /**
   * 获取角色
   *
   * @param id 角色ID
   * @return 返回角色
   */
  public SysRole get(String id) {
    SysRole role = getByPK(id);
    return role != null && checkOrgLevel(role.getOrgId()) ? role : null;
  }

  /**
   * 创建角色
   *
   * @param role 角色信息
   */
  public SysRole create(SysRole role) {
    requireOrgExist(role.getOrgId());
    checkRolePermission(role);
    role.setId(IdUtils.uuid());
    role.setOrgId(Checker.checkNotBlank(role.getOrgId(), currentOrgId()));
    role.setCreatorId(currentUserId());
    role.setCreateTime(new Date());
    role.setActive(Boolean.TRUE);
    super.insert(role);
    return role;
  }

  /**
   * 更新角色
   *
   * @param role 角色信息
   * @return 返回更新的数据
   */
  public SysRole update(SysRole role) {
    SysRole existRole = getByPK(role.getId());
    if (existRole == null) {
      throw new LogicException("无法发现角色");
    }
    checkRolePermission(role);

    existRole.setName(role.getName());
    existRole.setRemarks(role.getRemarks());
    existRole.setUpdateTime(new Date());
    super.updateByPKSelective(existRole);
    return existRole;
  }

  /**
   * 删除角色
   *
   * @param id    角色ID
   * @param force 是否强制删除
   * @return 返回删除条数，如果被删除成功，应该返回 1, 否则返回 0
   */
  public int delete(String id, boolean force) {
    SysRole role = getByPK(id);
    if (role != null) {
      checkRolePermission(role);
      // 检查被关联的用户
      if (uarService.countUserByRoles(Collections.singletonList(id)) > 0) {
        if (force) {
          // 强制删除关联的角色信息
          uarService.delete(new SysUserAndRole(null, id));
        } else {
          throw new LogicException("用户拥有此角色，请先取消授权!");
        }
      }
      return super.deleteByPK(role.getId());
    }
    return 0;
  }

  /**
   * 改变角色可用状态
   *
   * @param id     角色ID
   * @param active 状态
   * @return 返回是否更新
   */
  public boolean changeActive(String id, Boolean active) {
    SysRole role = get(id);
    if (role != null) {
      role.setActive(Checker.checkNotNull(active, role.getActive()));
      role.setUpdateTime(new Date());
      return updateByPKSelective(role) > 0;
    }
    return false;
  }

  @Override
  public List<SysRole> getList(SysRole condition, Date startTime, Date endTime, boolean multiLevel) {
    return getMapper().selectList(condition, startTime, endTime, multiLevel);
  }

  /**
   * 获取机构的角色
   *
   * @param orgId      机构ID
   * @param active     是否可用
   * @param multiLevel 多层级(当前用户机构下的所有子级机构)
   * @return 返回角色列表
   */
  public List<SysRole> getRoleList(String orgId, Boolean active, Boolean multiLevel) {
    if (Boolean.TRUE.equals(multiLevel)) {
      SysRole role = new SysRole();
      role.setOrgId(orgId);
      role.setActive(active);
      return getMapper().selectList(role, null, null, true);
    }
    return getMapper().selectPlainList(orgId, active);
  }

}
