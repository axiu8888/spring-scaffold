package com.benefitj.system.filter;

import com.benefitj.spring.security.jwt.token.JwtToken;
import com.benefitj.spring.security.jwt.token.JwtTokenManager;
import com.benefitj.system.service.SysOrganizationService;
import com.benefitj.system.utils.Const;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 机构过滤器
 */
@Component
public class OrganizationtRequestFilter extends OncePerRequestFilter {

  @Autowired
  SysOrganizationService organizationService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    JwtToken token = JwtTokenManager.currentToken();
    try {
      if (token != null && StringUtils.isNotBlank(token.getOrgId())) {
        token.put(Const.ORGANIZATION, organizationService.get(token.getOrgId()));
      }
    } catch (Exception ignore) { /* ~ */ } finally {
      filterChain.doFilter(request, response);
      if (token != null) {
        token.remove(Const.ORGANIZATION);
      }
    }
  }
}
