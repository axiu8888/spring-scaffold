package com.benefitj.system.mapper;

import com.benefitj.scaffold.common.mapper.SuperMapper;
import com.benefitj.system.model.SysOrgAndRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 机构和角色关联
 */
@Mapper
public interface SysOrgAndRoleMapper extends SuperMapper<SysOrgAndRole> {
}
