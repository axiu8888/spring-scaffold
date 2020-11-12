package com.benefitj.system.mapper;

import com.benefitj.scaffold.mapper.SuperMapper;
import com.benefitj.system.model.SysRoleAndUserGroup;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色和用户组关联
 */
@Mapper
public interface SysRoleAndUserGroupMapper extends SuperMapper<SysRoleAndUserGroup> {
}
