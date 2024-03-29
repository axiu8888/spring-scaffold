package com.benefitj.system.service;

import com.benefitj.core.IdUtils;
import com.benefitj.core.SortedTree;
import com.benefitj.scaffold.BaseService;
import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.security.CurrentUserService;
import com.benefitj.scaffold.security.exception.PermissionException;
import com.benefitj.spring.BeanHelper;
import com.benefitj.spring.mvc.query.PageRequest;
import com.benefitj.spring.security.jwt.token.JwtToken;
import com.benefitj.spring.security.jwt.token.JwtTokenManager;
import com.benefitj.system.mapper.SysOrganizationMapper;
import com.benefitj.system.model.SysOrg;
import com.benefitj.system.vo.OrgTreeVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 机构
 */
@Service
public class SysOrganizationService extends BaseService<SysOrg, SysOrganizationMapper>
    implements CurrentUserService {

  @Autowired
  private SysOrganizationMapper mapper;
  /**
   * 检查机构的层级
   */
  @Value("#{ @environment['com.benefitj.security.rbac.check-org-level'] ?: true }")
  private Boolean checkOrgLevel = Boolean.TRUE;

  @Override
  protected SysOrganizationMapper getMapper() {
    return mapper;
  }

  /**
   * 获取下一个机构的ID
   */
  private String getNextOrgId() {
    String orgId = null;
    for (int i = 0; i < 10000; i++) {
      String id = IdUtils.nextId(12);
      if (countById(id) < 1) {
        orgId = id;
        break;
      }
    }
    if (orgId == null) {
      throw new IllegalStateException("无法获取到机构ID");
    }
    return orgId;
  }

  /**
   * 获取 autoCode
   *
   * @param org 机构
   * @return 返回新的 autoCode
   */
  private String getAutoCode(SysOrg org) {
    String parentId = org.getParentId();
    return getAutoCode(parentId, org.getOrgId(), StringUtils.isNotBlank(parentId));
  }

  /**
   * 获取 autoCode
   *
   * @param parentId   父机构
   * @param id         机构ID
   * @param allowThrow 如果父机构不存在，是否抛出异常
   * @return 返回新的 autoCode
   */
  private String getAutoCode(String parentId, String id, boolean allowThrow) {
    SysOrg pOrg = getByPK(parentId);
    if (pOrg == null && allowThrow) {
      throw new IllegalStateException("父级机构不存在");
    }
    String parentAutoCode = Checker.checkNotNull(pOrg, SysOrg::getAutoCode);
    return SysOrg.generateAutoCode(parentAutoCode, id);
  }

  /**
   * 通过机构ID查询
   *
   * @param id 机构ID
   * @return 返回条数
   */
  public int countById(String id) {
    return StringUtils.isNotBlank(id) ? countByPK(id) : 0;
  }

  /**
   * 通过机构ID查询 autoCode
   *
   * @param id 机构ID
   * @return 返回查询的 autoCode
   */
  public String getAutoCodeById(String id) {
    return getMapper().selectAutoCodeById(id);
  }

  /**
   * 检查机构层级关系
   *
   * @param checkId 被检查的机构ID
   * @return 返回是否可操作机构
   */
  public boolean checkOrgLevel(String checkId) {
    return checkOrgLevel(checkId, currentOrgId());
  }

  /**
   * 检查机构层级关系
   *
   * @param checkId    被检查的机构ID
   * @param standardId 比较的机构
   * @return 返回是否可操作机构
   */
  public boolean checkOrgLevel(String checkId, String standardId) {
    return checkOrgLevel(Boolean.TRUE.equals(checkOrgLevel), checkId, standardId);
  }

  /**
   * 检查机构层级关系
   *
   * @param checkLevel 是否检查机构的层级
   * @param checkId    被检查的机构ID
   * @param standardId 比较的机构
   * @return 返回是否可操作机构
   */
  public boolean checkOrgLevel(boolean checkLevel, String checkId, String standardId) {
    if (checkLevel) {
      if (StringUtils.isAnyBlank(standardId, checkId)) {
        return false;
      }
      if (standardId.equals(checkId)) {
        return true;
      }
      JwtToken token = JwtTokenManager.currentToken();
      String ownedAutoCode = token != null && token.containsKey("organization")
          ? ((SysOrg) token.get("organization")).getAutoCode() : getAutoCodeById(standardId);
      String autoCode = getAutoCodeById(checkId);
      return StringUtils.isNotBlank(autoCode) && autoCode.startsWith(ownedAutoCode);
    }
    return true;
  }

  /**
   * 获取机构
   *
   * @param id 机构ID
   * @return 返回查询到的机构
   */
  public SysOrg get(String id) {
    return StringUtils.isNotBlank(id) && checkOrgLevel(id)
        ? getMapper().selectByPrimaryKey(id) : null;
  }

  /**
   * 创建新的机构
   */
  public SysOrg create(SysOrg org) {
    // 生成ID
    String orgId = getNextOrgId();
    org.setId(orgId);
    // 生成 autoCode
    String autoCode = getAutoCode(org);

    // 检查组织机构的层级，不建议超过10层
    String[] split = autoCode.split(SysOrg.AUTO);
    if (split.length > 10) {
      throw new IllegalStateException("组织机构不能超过超过10层");
    }

    org.setAutoCode(autoCode);
    org.setCreatorId(currentUserId());
    org.setCreateTime(new Date());
    org.setActive(true);
    super.insert(org);
    return org;
  }

  /**
   * 更新机构
   *
   * @param org 更新的机构
   * @return 返回更新的机构
   */
  public SysOrg update(SysOrg org) {
    SysOrg existOrg = getByPK(org.getId());
    if (existOrg == null) {
      throw new IllegalArgumentException("无法发现机构");
    }
    if (!checkOrgLevel(org.getId())) {
      throw new PermissionException("无法更新机构数据");
    }
    existOrg.setName(org.getName());
    existOrg.setCode(org.getCode());
    existOrg.setLogo(org.getLogo());
    existOrg.setUpdateTime(new Date());
    super.updateByPKSelective(existOrg);
    return existOrg;
  }

  /**
   * 删除机构
   *
   * @param id    机构ID
   * @param force 是否强制删除
   * @return 返回删除的条数
   */
  public int delete(String id, boolean force) {
    if (!checkOrgLevel(id)) {
      throw new PermissionException("无法删除机构");
    }
    // 删除机构
    return super.deleteByPK(id);
  }

  /**
   * 改变机构状态
   *
   * @param id     机构ID
   * @param active 状态
   * @return 返回是否更新
   */
  public boolean changeActive(String id, Boolean active) {
    SysOrg org = get(id);
    if (org != null) {
      org.setActive(Checker.checkNotNull(active, org.getActive()));
      org.setUpdateTime(new Date());
      return checkOrgLevel(id) && updateByPKSelective(org) > 0;
    }
    return false;
  }

  @Override
  public List<SysOrg> getList(SysOrg condition, Date startTime, Date endTime, Boolean multiLevel) {
    return getMapper().selectList(condition, startTime, endTime, Boolean.TRUE.equals(multiLevel));
  }

  /**
   * 获取机构分页，只返回自己拥有的子级机构，不包含当前机构
   *
   * @param page 分页参数
   * @return 返回分页数据
   */
  @Override
  public PageInfo<SysOrg> getPage(PageRequest<SysOrg> page) {
    SysOrg c = page.getCondition();
    String ownedId = currentOrgId();
    if (StringUtils.isBlank(c.getParentId())) {
      c.setParentId(ownedId);
    } else {
      // 检查查询的机构是否为当前机构的子级机构
      if (!checkOrgLevel(c.getParentId(), ownedId)) {
        c.setParentId(ownedId);
      }
    }
    if (page.isMultiLevel()) {
      // 重置autoCode
      c.setAutoCode(getAutoCodeById(c.getParentId()));
    }
    return super.getPage(page);
  }

  /**
   * 使用正则表达式匹配符合的AutoCode
   *
   * @param page 分页参数
   * @param n    至少匹配的次数(0 ~ ∞)
   * @param m    之多匹配的次数
   * @return 返回符合的机构
   */
  protected PageInfo<SysOrg> getByAutoCodeRegex(PageRequest<SysOrg> page, int n, int m) {
    SysOrg c = page.getCondition();
    // 重置autoCode
    c.setAutoCode(getAutoCodeById(c.getParentId()));
    // ORDER BY
    String orderBy = String.join(",", getOrderByList(page.getOrderBy()));
    // 查询分页
    n = Math.max(0, n);
    m = Math.max(n, m);
    Date startTime = page.getStartTime();
    Date endTime = page.getEndTime();
    // 分页
    PageHelper.startPage(page.getPageNum(), page.getPageSize(), orderBy);
    List<SysOrg> list = getMapper().selectByAutoCodeRegex(c, startTime, endTime, n, m);
    return PageInfo.of(list);
  }

  /**
   * 获取组织机构树
   *
   * @param id     机构ID
   * @param active 可用状态
   * @return 返回组织机构树
   */
  public List<SysOrg> getOrgTreeList(String id, @Nullable Boolean active) {
    String autoCode = getAutoCodeById(id);
    if (StringUtils.isBlank(autoCode)) {
      return Collections.emptyList();
    }
    SysOrg org = new SysOrg();
    org.setAutoCode(autoCode);
    org.setActive(active);
    List<SysOrg> list = getMapper().selectByAutoCodeRegex(org, null, null, 0, 10);
    final Map<String, OrgTreeVo> map = new ConcurrentHashMap<>(list.size());
    for (SysOrg o : list) {
      map.put(o.getId(), BeanHelper.copy(o, OrgTreeVo.class));
    }
    Map<String, OrgTreeVo> sort = SortedTree.sort(map);
    return new ArrayList<>(sort.values());
  }

}
