package com.benefitj.system.service;

import com.benefitj.core.HexUtils;
import com.benefitj.scaffold.common.LogicException;
import com.benefitj.scaffold.security.JwtUserDetailsService;
import com.benefitj.scaffold.security.token.JwtToken;
import com.benefitj.scaffold.security.token.JwtTokenManager;
import com.benefitj.scaffold.security.user.JwtUserDetails;
import com.benefitj.scaffold.vo.AuthTokenVo;
import com.benefitj.spring.BeanHelper;
import com.benefitj.system.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户认证(登录，注册)
 */
@Service
public class UserAuthenticationService implements JwtUserDetailsService {

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtTokenManager jwtTokenManager;
  @Autowired
  private SysUserService userService;
  @Autowired
  private SysUserAndRoleService uarService;

  @Override
  public JwtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    JwtToken token = JwtTokenManager.currentToken(true);
    if (token != null
        && token.getUserDetails().getUsername().equals(username)) {
      return token.getUserDetails();
    }
    SysUser user = userService.getByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("Username not found");
    }
    return getUserDetails(user);
  }

  public AuthTokenVo<SysUser> register(String username, String password, String orgId) {
    SysUser user = new SysUser();
    user.setUsername(username);
    user.setPassword(password);
    user.setOrgId(orgId);

    // 保存用户
    user = save(user);

    JwtUserDetails userDetails = getUserDetails(user);
    JwtToken accessToken = jwtTokenManager.createAccessToken(userDetails);
    JwtToken refreshToken = jwtTokenManager.createRefreshToken(userDetails);

    AuthTokenVo<SysUser> vo = new AuthTokenVo<>();
    vo.setAccessToken(accessToken.getRawToken());
    vo.setRefreshToken(refreshToken.getRawToken());
    vo.setData(user);
    return vo;
  }

  /**
   * 保存用户信息
   *
   * @param user
   */
  @Transactional(rollbackFor = Throwable.class)
  public SysUser save(SysUser user) {
    if (StringUtils.isNotBlank(user.getId())) {
      userService.update(user);
    } else {
      userService.create(user);
    }
    return user;
  }

  /**
   * 通过用户名和密码登录
   *
   * @param username 用户名
   * @param password 密码
   * @return 返回登陆后的 token
   */
  public AuthTokenVo login(String username, String password) {
    SysUser user = userService.getByUsername(username);
    String rawPassword = HexUtils.bytesToHex(password.getBytes());
    if (user == null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
      throw new LogicException("账号或密码错误");
    }

    if (!Boolean.TRUE.equals(user.getActive())) {
      throw new LogicException("账号不可用");
    }

    if (Boolean.TRUE.equals(user.getLocked())) {
      throw new LogicException("账号被锁定");
    }

    JwtUserDetails userDetails = getUserDetails(user);

    JwtToken accessJwtToken = jwtTokenManager.createAccessToken(userDetails);
    JwtToken refreshJwtToken = jwtTokenManager.createRefreshToken(userDetails);
    AuthTokenVo vo = new AuthTokenVo();
    vo.setAccessToken(accessJwtToken.getRawToken());
    vo.setRefreshToken(refreshJwtToken.getRawToken());
    return vo;
  }

  /**
   * 通过用户ID获取用户信息详情
   *
   * @param userId 用户ID
   * @return 返回用户详情
   */
  @Override
  public JwtUserDetails getUserDetails(String userId) {
    JwtToken token = JwtTokenManager.currentToken(true);
    if (token != null
        && token.getUserDetails().getUserId().equals(userId)) {
      return token.getUserDetails();
    }
    SysUser user = userService.get(userId);
    if (user == null) {
      throw new UsernameNotFoundException("The user is not found.");
    }
    return getUserDetails(user);
  }

  /**
   * 获取用户信息详情
   *
   * @param user 用户
   * @return 返回用户详情
   */
  public JwtUserDetails getUserDetails(SysUser user) {
    // 转换为 UserDetails，并查询用户的角色(权限)
    JwtUserDetails userDetails = BeanHelper.copy(user, JwtUserDetails.class);
    List<GrantedAuthority> authorityList = uarService.getRoleByUserId(user.getId())
        .stream()
        // 过滤不可用的角色
        //.filter(r -> Boolean.TRUE.equals(r.getActive()))
        .flatMap(r -> Stream.of(BeanHelper.copy(r, GrantedAuthority.class)))
        .collect(Collectors.toList());
    userDetails.setAuthorities(authorityList);
    return userDetails;
  }

  /**
   * 通过RefreshToken获取新的AccessToken
   *
   * @param refreshToken 刷新的token
   * @return 返回access token
   */
  public AuthTokenVo getAccessToken(String refreshToken) {
    JwtToken jwtToken = jwtTokenManager.parse(refreshToken, false, true);
    if (!jwtToken.isRefresh()) {
      throw new LogicException("refreshToken错误");
    }

    JwtUserDetails userDetails = getUserDetails(jwtToken.getUserId());
    JwtToken accessToken = jwtTokenManager.createAccessToken(userDetails);
    AuthTokenVo vo = new AuthTokenVo();
    vo.setAccessToken(accessToken.getRawToken());
    vo.setRefreshToken(refreshToken);
    return vo;
  }


}
