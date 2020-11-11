package com.benefitj.system.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.benefitj.core.SortedTree;
import com.benefitj.system.model.SysOrganization;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 组织机构树
 */
public class OrgTreeVo extends SysOrganization implements SortedTree.Tree<String, OrgTreeVo> {

  private final Set<OrgTreeVo> children = new LinkedHashSet<>();

  @Override
  public Set<OrgTreeVo> getChildren() {
    return children;
  }

  @JSONField(serialize = false)
  @JsonIgnore
  @Override
  public String getOrgName() {
    return super.getOrgName();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    OrgTreeVo orgTreeVo = (OrgTreeVo) o;
    return Objects.equals(getId(), orgTreeVo.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

}
