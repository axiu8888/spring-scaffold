package com.benefitj.system.vo;

import com.benefitj.system.model.SysRole;
import org.springframework.security.core.GrantedAuthority;

/**
 * 权限
 */
public class JwtGrantedAuthority extends SysRole implements GrantedAuthority {

  @Override
  public String getAuthority() {
    return getId();
  }

}
