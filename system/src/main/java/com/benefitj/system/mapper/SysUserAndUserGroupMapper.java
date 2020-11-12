package com.benefitj.system.mapper;

import com.benefitj.scaffold.mapper.SuperMapper;
import com.benefitj.system.model.SysUserAndUserGroup;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户和用户组关联
 */
@Mapper
public interface SysUserAndUserGroupMapper extends SuperMapper<SysUserAndUserGroup> {
}
