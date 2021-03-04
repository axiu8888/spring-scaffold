package com.benefitj.system.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.benefitj.scaffold.security.user.JwtUserDetails;
import com.benefitj.system.model.SysUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.List;

/**
 * JWT用户详情
 */
public class SimpleJwtUserDetails extends SysUser implements JwtUserDetails {

  private List<GrantedAuthority> authorities = Collections.emptyList();

  /**
   * 获取用户ID
   */
  @JsonIgnore
  @JSONField(serialize = false, deserialize = false)
  @Override
  public String getUserId() {
    return getId();
  }

  /**
   * 设置用户ID
   *
   * @param userId 用户ID
   */
  @JsonIgnore
  @JSONField(serialize = false, deserialize = false)
  @Override
  public void setUserId(String userId) {
    this.setId(userId);
  }

  @Override
  public void setAuthorities(List<GrantedAuthority> authorities) {
    this.authorities = authorities;
  }

  /**
   * 获取权限
   */
  @Override
  public List<GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return Boolean.FALSE.equals(getLocked());
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return Boolean.TRUE.equals(getActive());
  }
}
