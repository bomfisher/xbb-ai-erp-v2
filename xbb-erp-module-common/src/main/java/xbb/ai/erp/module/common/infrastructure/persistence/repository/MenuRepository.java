package xbb.ai.erp.module.common.infrastructure.persistence.repository;

import xbb.ai.erp.module.common.infrastructure.persistence.po.MenuPO;

import java.util.List;
import java.util.Map;

public interface MenuRepository {
    List<MenuPO> findByCondition(Map<String, Object> conditionMap);
}
