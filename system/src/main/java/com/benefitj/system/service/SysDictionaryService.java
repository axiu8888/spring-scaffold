package com.benefitj.system.service;

import com.benefitj.core.IdUtils;
import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.security.exception.PermissionException;
import com.benefitj.system.mapper.SysDictionaryMapper;
import com.benefitj.system.model.SysDictionaryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 字典
 */
@Service
public class SysDictionaryService extends SysBaseService<SysDictionaryModel, SysDictionaryMapper> {

  @Autowired
  private SysDictionaryMapper mapper;

  @Override
  protected SysDictionaryMapper getMapper() {
    return mapper;
  }

  public boolean checkDictionaryPermission(SysDictionaryModel dictionary) throws PermissionException {
    if (!super.checkPermission(dictionary)) {
      logger.info("当前用户无权操作此机构的字典, checkId: {}, standardId: {}", dictionary.getOrgId(), currentOrgId());
      throw new PermissionException("当前用户无权操作此字典");
    }
    return false;
  }

  @Override
  protected void checkRecord(SysDictionaryModel record) {
    Checker.requireNotBlank(record.getName(), "字典名不能为空");
    Checker.requireNotBlank(record.getCode(), "字典代码不能为空");
    Checker.requireNotBlank(record.getAttribute(), "字典属性不能为空");
    Checker.requireNotBlank(record.getValue(), "字典属性值不能为空");
  }

  /**
   * 获取字典
   *
   * @param id 字典ID
   * @return 返回字典
   */
  public SysDictionaryModel get(String id) {
    SysDictionaryModel dictionary = getByPK(id);
    return dictionary != null && checkPermission(dictionary) ? dictionary : null;
  }

  /**
   * 创建字典
   *
   * @param dictionary 字典信息
   */
  public SysDictionaryModel create(SysDictionaryModel dictionary) {
    requireOrgExist(dictionary.getOrgId());
    checkDictionaryPermission(dictionary);

    dictionary.setId(IdUtils.uuid());
    dictionary.setOrgId(Checker.checkNotBlank(dictionary.getOrgId(), currentOrgId()));
    dictionary.setCreatorId(currentUserId());
    dictionary.setCreateTime(new Date());
    dictionary.setActive(Boolean.TRUE);
    super.insert(dictionary);
    return dictionary;
  }

  /**
   * 更新字典
   *
   * @param dictionary 字典信息
   * @return 返回更新的数据
   */
  public SysDictionaryModel update(SysDictionaryModel dictionary) {
    SysDictionaryModel existDictionary = getByPK(dictionary.getId());
    if (existDictionary == null) {
      throw new IllegalStateException("无法发现字典");
    }
    checkDictionaryPermission(dictionary);

    existDictionary.setParentId(dictionary.getParentId());
    existDictionary.setName(dictionary.getName());
    existDictionary.setCode(dictionary.getCode());
    existDictionary.setAttribute(dictionary.getAttribute());
    existDictionary.setValue(dictionary.getValue());
    existDictionary.setRemarks(dictionary.getRemarks());
    existDictionary.setUpdateTime(new Date());
    super.updateByPKSelective(existDictionary);
    return existDictionary;
  }

  /**
   * 删除字典
   *
   * @param id 字典ID
   * @return 返回删除条数，如果被删除成功，应该返回 1, 否则返回 0
   */
  public int delete(String id, boolean force) {
    SysDictionaryModel dictionary = getByPK(id);
    if (dictionary != null) {
      checkDictionaryPermission(dictionary);
      return super.deleteByPK(dictionary.getId());
    }
    return 0;
  }

  /**
   * 改变字典可用状态
   *
   * @param id     字典ID
   * @param active 状态
   * @return 返回是否更新
   */
  public boolean changeActive(String id, Boolean active) {
    SysDictionaryModel dictionary = get(id);
    if (dictionary != null) {
      checkDictionaryPermission(dictionary);

      dictionary.setActive(Checker.checkNotNull(active, dictionary.getActive()));
      dictionary.setUpdateTime(new Date());
      return updateByPKSelective(dictionary) > 0;
    }
    return false;
  }

}
